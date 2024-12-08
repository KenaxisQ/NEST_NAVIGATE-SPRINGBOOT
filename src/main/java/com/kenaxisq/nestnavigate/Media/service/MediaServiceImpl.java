package com.kenaxisq.nestnavigate.Media.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.hierynomus.msdtyp.AccessMask;
import com.hierynomus.msfscc.fileinformation.FileIdBothDirectoryInformation;
import com.hierynomus.mssmb2.SMB2CreateDisposition;
import com.hierynomus.mssmb2.SMB2ShareAccess;
import com.kenaxisq.nestnavigate.Media.dto.MediaReadDto;
import com.kenaxisq.nestnavigate.Media.dto.MediaUploadDto;
import com.kenaxisq.nestnavigate.Media.entity.Media;
import com.kenaxisq.nestnavigate.Media.util.ImageHandler;
import com.kenaxisq.nestnavigate.Media.util.ProcessedImage;
import com.kenaxisq.nestnavigate.custom_exceptions.ApiException;
import com.kenaxisq.nestnavigate.custom_exceptions.ErrorCodes;
import com.kenaxisq.nestnavigate.user.entity.User;
import com.kenaxisq.nestnavigate.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;
import com.hierynomus.smbj.share.File;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

@Service
public class MediaServiceImpl implements MediaService {

    private final ObjectMapper objectMapper;
    @Value("${SMB_SERVER_ADDRESS}")
    private String SERVER;
    @Value("${SMB_SERVER_USERNAME}")
    private  String USERNAME;
    @Value("${SMB_SERVER_PASSWORD}")
    private String PASSWORD;
    @Value("${SMB_SERVER_SHARE_NAME_PROPERTIES}")
    private String SHARE_NAME_PROPERTIES;
    @Value("${SMB_SERVER_SHARE_NAME_USER_PROFILES}")
    private String SHARE_NAME_USER_PROFILES;
    private static final Logger logger = LoggerFactory.getLogger(MediaServiceImpl.class);
    private final UserService userService;
    private Gson gson = new Gson();

    @Autowired
    public MediaServiceImpl(UserService userService, ObjectMapper objectMapper) {
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    public List<Media> uploadFiles(MediaUploadDto mediaUploadDto) {
        List<Media> uploadedFiles = new ArrayList<>();
        boolean isProperty = mediaUploadDto.getIsProperty();
        String identifier = mediaUploadDto.getIdentifier();
        MultipartFile[] files = mediaUploadDto.getMedia();
        if(!isProperty){
            userService.getUser(mediaUploadDto.getIdentifier());
        }
        validateSingleFileUpload(isProperty, files);

        try (SMBClient client = new SMBClient();
             Connection connection = client.connect(SERVER);
             Session session = connection.authenticate(new AuthenticationContext(USERNAME, PASSWORD.toCharArray(), null))) {

            logger.info("Connected to SMB server.");
            String shareName = isProperty ? SHARE_NAME_PROPERTIES : SHARE_NAME_USER_PROFILES;
            DiskShare share = (DiskShare) session.connectShare(shareName);

            for (MultipartFile file : files) {
                String destinationPath = determineDestinationPath(isProperty, identifier, file.getOriginalFilename());

                uploadFileToShare(file, share, destinationPath, identifier, isProperty, uploadedFiles);
            }

        } catch (IOException e) {
            logger.error("Failed to connect to SMB server: {}", e.getMessage());
            throw new ApiException(ErrorCodes.CONNECTION_ERROR.getCode(), "Failed to connect to SMB server", HttpStatus.SERVICE_UNAVAILABLE);
        }

        logger.info("Successfully uploaded files.");
        if(!isProperty) {
            userService.updateProfilePicture(identifier, gson.toJson(uploadedFiles.get(0)));
        }
        return uploadedFiles;
    }

    public List<byte[]> readImage(MediaReadDto mediaReadDto) {
        boolean isProperty = mediaReadDto.isProperty();
        String identifier = mediaReadDto.getIdentifier();
        String fileName = mediaReadDto.getFileName();

        try (SMBClient client = new SMBClient();
            Connection connection = client.connect(SERVER);
            Session session = connection.authenticate(new AuthenticationContext(USERNAME, PASSWORD.toCharArray(), null))) {
            String shareName = isProperty ? SHARE_NAME_PROPERTIES : SHARE_NAME_USER_PROFILES;
            DiskShare share = (DiskShare) session.connectShare(shareName);
            List<byte[]> requestedImages = new ArrayList<>();
            List<FileIdBothDirectoryInformation> directoryContents;
            String basePath = isProperty ? identifier : "";
            String searchPath = isProperty && fileName != null ? identifier + "/" + fileName : fileName;
            directoryContents = share.list(basePath);
            for (FileIdBothDirectoryInformation fileInfo : directoryContents) {
                String currentFileName = fileInfo.getFileName();
                if (!currentFileName.equals(".") && !currentFileName.equals("..")) {
                    String fullPath = basePath + "/" + currentFileName;
                    if (isProperty) {
                        if (fileName == null || fileName.trim().isEmpty()) {
                            if (isImageFile(currentFileName)) {
                                requestedImages.add(readFile(fullPath, share));
                            }
                        } else {
                            if (fullPath.equalsIgnoreCase(searchPath) && isImageFile(currentFileName)) {
                                requestedImages.add(readFile(fullPath, share));
                                return requestedImages;
                            }
                        }
                    }
                    else {
                        if (currentFileName.startsWith(identifier) && isImageFile(currentFileName)) {
                            requestedImages.add(readFile(fullPath, share));
                            return requestedImages;
                        }
                    }
                }
            }

            if (!requestedImages.isEmpty()) {
                return requestedImages;
            }
            throw new ApiException(ErrorCodes.FILE_NOT_FOUND);

        } catch (IOException e) {
            throw new ApiException("FILESERVER_CONN_ERROR", "error while connecting to file server", HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    private void validateSingleFileUpload(boolean isProperty, MultipartFile[] files) {
        if (!isProperty && files.length != 1) {
            throw new ApiException("Only Single File is Accepted!", "Only one file can be uploaded as user profile picture", HttpStatus.BAD_REQUEST);
        }
    }

    private String determineDestinationPath(boolean isProperty, String identifier, String originalFilename) {
        return isProperty ? identifier + "/" + originalFilename : identifier + getExtension(originalFilename);
    }

    private void uploadFileToShare(MultipartFile file, DiskShare share, String destinationPath, String identifier, boolean isProperty, List<Media> uploadedFiles) {
        try (ProcessedImage processedImage = ImageHandler.processFile(file)) {
            InputStream inputStream = processedImage.getInputStream();
            long compressedSize = processedImage.getSize();
            if (isProperty) {
                ensureDirectoriesForProperties(share, identifier);
            }
            try (File smbFile = share.openFile(destinationPath, EnumSet.of(AccessMask.GENERIC_WRITE), null, SMB2ShareAccess.ALL, SMB2CreateDisposition.FILE_OVERWRITE_IF, null)) {
                fileTransfer(inputStream, smbFile);
                Media media = new Media();
                media.setName(isProperty?identifier+"/"+file.getOriginalFilename():identifier + getExtension(Objects.requireNonNull(file.getOriginalFilename())));
                media.setType(file.getContentType());
                media.setPath(isProperty?"properties/"+ destinationPath:"user_profile/");
                media.setCompressedSize(formatFileSize(compressedSize));
                media.setSize(formatFileSize(file.getSize()));
                media.setIdentifier(identifier);
                media.setUploadDateTime(LocalDateTime.now().toString());
                uploadedFiles.add(media);

            } catch (IOException e) {
                logger.error("Failed to write file to SMB share: {}", e.getMessage());
                throw new ApiException(ErrorCodes.FILE_UPLOAD_ERROR.getCode(), "Failed to write file to SMB share", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            logger.error("Unable to process file: {}", e.getMessage());
            throw new ApiException(ErrorCodes.FILE_TOO_LARGE.getCode(), "Unable to process file", HttpStatus.BAD_REQUEST);
        }
    }

    private void fileTransfer(InputStream inputStream, File smbFile) throws IOException {
        byte[] buffer = new byte[8192];
        int bytesRead;
        long offset = 0;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            smbFile.write(buffer, offset, 0, bytesRead);
            offset += bytesRead;
        }
    }

    private String getExtension(String filename) {
        int index = filename.lastIndexOf('.');
        return index > 0 ? filename.substring(index) : "";
    }

    public static String formatFileSize(long sizeInBytes) {
        return String.format("%.2f KB", sizeInBytes / 1024.0);
    }

    private void ensureDirectoriesForProperties(DiskShare share, String identifier) {
        String[] directories = identifier.split("/");
        StringBuilder currentPath = new StringBuilder();

        for (String dir : directories) {
            if (dir.isEmpty()) continue;

            currentPath.append(dir).append("/");
            String path = currentPath.toString();

            if (!share.folderExists(path)) {
                share.mkdir(path);
            }
        }
    }

    private byte[] readFile(String filePath, DiskShare share) throws IOException {
        File smbFile = share.openFile(
                filePath,
                EnumSet.of(AccessMask.GENERIC_READ),
                null,
                SMB2ShareAccess.ALL,
                SMB2CreateDisposition.FILE_OPEN,
                null
        );

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[8192];
            long offset = 0;

            int bytesRead;
            while ((bytesRead = smbFile.read(buffer, offset)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                offset += bytesRead;
            }

            smbFile.close();
            return outputStream.toByteArray();
        }
    }

    public String deleteFile(MediaReadDto mediaReadDto) {
        logger.debug("Entering deleteFile method with MediaReadDto: {}", mediaReadDto);

        try (SMBClient client = new SMBClient();
             Connection connection = client.connect(SERVER);
             Session session = connection.authenticate(new AuthenticationContext(USERNAME, PASSWORD.toCharArray(), null))) {

            String filePath;
            if (mediaReadDto.isProperty() && mediaReadDto.getFileName() != null) {
                filePath = mediaReadDto.getIdentifier() + "/" + mediaReadDto.getFileName();
            } else if (mediaReadDto.isProperty()) {
                filePath = mediaReadDto.getIdentifier();
            } else {
                filePath = mediaReadDto.getFileName();
            }

            logger.info("Preparing to delete file at path: {}", filePath);
            DiskShare share = (DiskShare) session.connectShare(mediaReadDto.isProperty() ? SHARE_NAME_PROPERTIES : SHARE_NAME_USER_PROFILES);

            try {
                share.rm(filePath);
                logger.info("File successfully deleted: {}", filePath);
            } catch (Exception e) {
                logger.error("Failed to delete file at path: {}", filePath, e);
                throw new ApiException(ErrorCodes.FILE_NOT_FOUND); // Ensure to use appropriate error code
            }

        } catch (IOException ioe) {
            logger.error("Error connecting to file server", ioe);
            throw new ApiException("FILESERVER_CONN_ERROR", "Error while connecting to file server", HttpStatus.SERVICE_UNAVAILABLE);
        } catch (Exception ex) {
            logger.error("General error occurred while deleting file", ex);
            throw new ApiException(ErrorCodes.INTERNAL_SERVER_ERROR);
        }

        try {
            if (!mediaReadDto.isProperty()) {
                User user = userService.getUser(mediaReadDto.getIdentifier());
                user.setProfilePic(null);

                try {
                    userService.updateUser(user);
                    logger.info("User profile picture updated successfully for User: {}", user.getName());
                    return "Profile Picture Deleted Successfully for User: " + user.getName();
                } catch (Exception e) {
                    logger.error("Failed to update user profile picture for: {}", user.getName(), e);
                    throw new ApiException("USER_UPDATE_ERROR", "Failed to update user profile picture", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        } catch (Exception e) {
            logger.error("User not found or other error: {}", mediaReadDto.getIdentifier(), e);
            throw new ApiException(ErrorCodes.USER_NOT_FOUND); // Ensure to use appropriate error code
        }

        logger.debug("Exiting deleteFile method");
        return "Profile Picture Deleted Successfully for Property: " + mediaReadDto.getIdentifier();
    }
    private boolean isImageFile(String fileName) {
        String fileNameLower = fileName.toLowerCase();
        return fileNameLower.endsWith(".jpg") || fileNameLower.endsWith(".jpeg") || fileNameLower.endsWith(".png");
    }
}
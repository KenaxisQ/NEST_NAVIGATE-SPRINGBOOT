package com.kenaxisq.nestnavigate.Media.service;

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
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

@Service
public class MediaServiceImpl implements MediaService {

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
    @Autowired
    public MediaServiceImpl(UserService userService) {
        this.userService = userService;
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
        return uploadedFiles;
    }

    private void validateSingleFileUpload(boolean isProperty, MultipartFile[] files) {
        if (!isProperty && files.length != 1) {
            throw new ApiException("Only Single File is Accepted!", "Only one file can be uploaded when isProperty is false", HttpStatus.BAD_REQUEST);
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

    public byte[] readImage(MediaReadDto mediaReadDto) {
        boolean isProperty = mediaReadDto.isProperty();
        String identifier = mediaReadDto.getIdentifier();
        String fileName = mediaReadDto.getFileName();
        try (SMBClient client = new SMBClient();
             Connection connection = client.connect(SERVER);
             Session session = connection.authenticate(new AuthenticationContext(USERNAME, PASSWORD.toCharArray(), null))) {

            String shareName = isProperty ? SHARE_NAME_PROPERTIES : SHARE_NAME_USER_PROFILES;
            DiskShare share = (DiskShare) session.connectShare(shareName);
            String basePath = isProperty ? identifier : "";
            String searchPath = isProperty ? identifier + "/" + fileName : fileName;

            List<String> foundFiles = new ArrayList<>();
            List<FileIdBothDirectoryInformation> directoryContents = share.list(basePath);

            for (FileIdBothDirectoryInformation fileInfo : directoryContents) {
                String currentFileName = fileInfo.getFileName();
                if (!currentFileName.equals(".") && !currentFileName.equals("..")) {
                    String fullPath = basePath + "/" + currentFileName;
                    if (isProperty || fullPath.equalsIgnoreCase(searchPath)) {
                        if (isImageFile(currentFileName)) {
                            foundFiles.add(fullPath);
                        }
                    }
                }
            }

            if (!foundFiles.isEmpty()) {
                String filePath = foundFiles.get(0);
                return readFile(filePath, share);
            }
            throw new ApiException(ErrorCodes.FILE_NOT_FOUND);
        } catch (IOException e) {
            throw new ApiException("FILESERVER_CONN_ERROR", "error while connecting to file server", HttpStatus.SERVICE_UNAVAILABLE);
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

    public void deleteFile(String filePath) throws IOException {
        try (SMBClient client = new SMBClient();
             Connection connection = client.connect(SERVER);
             Session session = connection.authenticate(new AuthenticationContext(USERNAME, PASSWORD.toCharArray(), null))) {

            DiskShare share = (DiskShare) session.connectShare(SHARE_NAME_PROPERTIES);
            share.rm(filePath);
        }

    }

    private boolean isImageFile(String fileName) {
        String fileNameLower = fileName.toLowerCase();
        return fileNameLower.endsWith(".jpg") || fileNameLower.endsWith(".jpeg") || fileNameLower.endsWith(".png");
    }
}
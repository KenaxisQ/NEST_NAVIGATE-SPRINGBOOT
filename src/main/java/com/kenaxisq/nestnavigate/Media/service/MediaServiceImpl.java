package com.kenaxisq.nestnavigate.Media.service;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
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

import jakarta.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class MediaServiceImpl implements MediaService {

    private final ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(MediaServiceImpl.class);
    private final UserService userService;
    private final Gson gson = new Gson();
    private AmazonS3 s3Client;
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final long MAX_COMPRESSED_FILE_SIZE = 500 * 1024; // 500KB

    @Value("${UTHO_BASE_URL}")
    private String s3Endpoint;

    @Value("${UTHO_ACCESS_KEY}")
    private String accessKey;

    @Value("${UTHO_SECRET_KEY}")
    private String secretKey;

    @Value("${UTHO_REGION}")
    private String region;

    @Value("${UTHO_BUCKET_PROPERTIES}")
    private String propertiesBucket;

    @Value("${UTHO_BUCKET_USER_PROFILES}")
    private String profilesBucket;

    @Autowired
    public MediaServiceImpl(UserService userService, ObjectMapper objectMapper) {
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void initializeS3Client() throws ApiException {
        try {
            BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

            ClientConfiguration clientConfig = new ClientConfiguration();
            clientConfig.setConnectionTimeout(30000);
            clientConfig.setSocketTimeout(30000);

            this.s3Client = AmazonS3ClientBuilder.standard()
                    .withEndpointConfiguration(
                            new AwsClientBuilder.EndpointConfiguration(s3Endpoint, region)
                    )
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withClientConfiguration(clientConfig)
                    .withPathStyleAccessEnabled(true)
                    .disableChunkedEncoding()
                    .build();

            // Ensure buckets exist
            createBucketIfNotExists(propertiesBucket);
            createBucketIfNotExists(profilesBucket);

        } catch (Exception e) {
            logger.error("Failed to initialize S3 client: {}", e.getMessage());
            throw new ApiException(ErrorCodes.CONNECTION_ERROR.getCode(), "Failed to connect to S3 server", HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    private void createBucketIfNotExists(String bucketName) {
        if (!s3Client.doesBucketExistV2(bucketName)) {
            s3Client.createBucket(bucketName);
        }
    }

    public List<Media> uploadFiles(MediaUploadDto mediaUploadDto) {
        List<Media> uploadedFiles = new ArrayList<>();
        boolean isProperty = mediaUploadDto.getIsProperty();
        String identifier = mediaUploadDto.getIdentifier();
        MultipartFile[] files = mediaUploadDto.getMedia();

        if (!isProperty) {
            userService.getUser(mediaUploadDto.getIdentifier());
        }
        validateSingleFileUpload(isProperty, files);

        String bucketName = isProperty ? propertiesBucket : profilesBucket;

        for (MultipartFile file : files) {
            String destinationPath = determineDestinationPath(isProperty, identifier, file.getOriginalFilename());
            uploadFileToS3(file, bucketName, destinationPath, identifier, isProperty, uploadedFiles);
        }

        logger.info("Successfully uploaded files to S3.");
        if (!isProperty) {
            userService.updateProfilePicture(identifier, uploadedFiles.get(0).getPath() + uploadedFiles.get(0).getName());
        }
        return uploadedFiles;
    }

    public List<byte[]> readImage(MediaReadDto mediaReadDto) {
        Boolean isProperty = mediaReadDto.getIsProperty();
        String identifier = mediaReadDto.getIdentifier();
        String fileName = mediaReadDto.getFileName();
        String bucketName = isProperty ? propertiesBucket : profilesBucket;
        List<byte[]> requestedImages = new ArrayList<>();

        try {
            if (isProperty && fileName != null) {
                // Read specific property image
                String key = identifier + "/" + fileName;
                requestedImages.add(readFileFromS3(bucketName, key));
            } else if (isProperty) {
                // List and read all property images
                ListObjectsV2Result result = s3Client.listObjectsV2(bucketName, identifier + "/");
                for (S3ObjectSummary object : result.getObjectSummaries()) {
                    if (isImageFile(object.getKey())) {
                        requestedImages.add(readFileFromS3(bucketName, object.getKey()));
                    }
                }
            } else {
                // Read user profile image
                ListObjectsV2Result result = s3Client.listObjectsV2(bucketName);
                for (S3ObjectSummary object : result.getObjectSummaries()) {
                    if (object.getKey().startsWith(identifier) && isImageFile(object.getKey())) {
                        requestedImages.add(readFileFromS3(bucketName, object.getKey()));
                        break;
                    }
                }
            }

            if (requestedImages.isEmpty()) {
                throw new ApiException(ErrorCodes.FILE_NOT_FOUND);
            }

            return requestedImages;

        } catch (AmazonS3Exception e) {
            logger.error("S3 error while reading image: {}", e.getMessage());
            throw new ApiException("S3_READ_ERROR", "Error while reading from S3", HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    private byte[] readFileFromS3(String bucketName, String key) throws AmazonS3Exception {
        S3Object object = s3Client.getObject(bucketName, key);
        try (S3ObjectInputStream inputStream = object.getObjectContent()) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new ApiException("S3_READ_ERROR", "Error while reading file from S3", HttpStatus.INTERNAL_SERVER_ERROR);
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

    private void uploadFileToS3(MultipartFile file, String bucketName, String destinationPath, String identifier, boolean isProperty, List<Media> uploadedFiles) {
        try (ProcessedImage processedImage = ImageHandler.processFile(file)) {
            InputStream inputStream = processedImage.getInputStream();
            long compressedSize = processedImage.getSize();

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(compressedSize);

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, destinationPath, inputStream, metadata);
            s3Client.putObject(putObjectRequest);

            Media media = new Media();
            media.setName(isProperty ?file.getOriginalFilename() : identifier + getExtension(Objects.requireNonNull(file.getOriginalFilename())));
            media.setType(file.getContentType());
            media.setPath(isProperty ? s3Endpoint+ "/Properties/" + destinationPath : s3Endpoint+ "/User_Profiles/");
            media.setCompressedSize(formatFileSize(compressedSize));
            media.setSize(formatFileSize(file.getSize()));
            media.setIdentifier(identifier);
            media.setUploadDateTime(LocalDateTime.now().toString());
            uploadedFiles.add(media);

        } catch (Exception e) {
            logger.error("Failed to upload file to S3: {}", e.getMessage());
            throw new ApiException(ErrorCodes.FILE_UPLOAD_ERROR.getCode(), "Failed to upload file to S3", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public String deleteFile(MediaReadDto mediaReadDto) {
            logger.debug("Entering deleteFile method with MediaReadDto: {}", mediaReadDto);

        try {
            String bucketName = mediaReadDto.getIsProperty() ? propertiesBucket : profilesBucket;
            String key;

            if (mediaReadDto.getIsProperty() && mediaReadDto.getFileName() != null) {
                key = mediaReadDto.getIdentifier() + "/" + mediaReadDto.getFileName();
            } else if (mediaReadDto.getIsProperty()) {
                // Delete all files in the property folder
                ListObjectsV2Result result = s3Client.listObjectsV2(bucketName, mediaReadDto.getIdentifier() + "/");
                for (S3ObjectSummary object : result.getObjectSummaries()) {
                    s3Client.deleteObject(bucketName, object.getKey());
                }
                return "All files deleted successfully for Property: " + mediaReadDto.getIdentifier();
            } else {
                key = mediaReadDto.getFileName();
            }

            s3Client.deleteObject(bucketName, key);

            if (!mediaReadDto.getIsProperty()) {
                User user = userService.getUser(mediaReadDto.getIdentifier());
                user.setProfilePic(null);
                userService.updateUser(user);
                return "Profile Picture Deleted Successfully for User: " + user.getName();
            }

            return "File Deleted Successfully for Property: " + mediaReadDto.getIdentifier();

        } catch (AmazonS3Exception e) {
            logger.error("S3 error while deleting file: {}", e.getMessage());
            throw new ApiException("S3_DELETE_ERROR", "Error while deleting from S3", HttpStatus.SERVICE_UNAVAILABLE);
        } catch (Exception e) {
            logger.error("Error during file deletion: {}", e.getMessage());
            throw new ApiException(ErrorCodes.INTERNAL_SERVER_ERROR);
        }
    }

    private String getExtension(String filename) {
        int index = filename.lastIndexOf('.');
        return index > 0 ? filename.substring(index) : "";
    }

    public static String formatFileSize(long sizeInBytes) {
        return String.format("%.2f KB", sizeInBytes / 1024.0);
    }

    private boolean isImageFile(String fileName) {
        String fileNameLower = fileName.toLowerCase();
        return fileNameLower.endsWith(".jpg") || fileNameLower.endsWith(".jpeg") || fileNameLower.endsWith(".png");
    }
}
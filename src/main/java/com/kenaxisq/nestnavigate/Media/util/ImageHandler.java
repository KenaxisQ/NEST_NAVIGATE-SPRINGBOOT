package com.kenaxisq.nestnavigate.Media.util;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;
import java.util.List;

public class ImageHandler {

    private static final long MAX_FILE_SIZE = 500 * 1024; // 500 KB
    private static final List<String> VALID_IMAGE_TYPES = List.of("image/jpeg", "image/png", "image/jpg");

    public static ProcessedImage processFile(MultipartFile file) throws IOException {
        validateFileType(file);
        ProcessedImage processedImage = new ProcessedImage(file.getInputStream(), file.getSize());
        InputStream fileInputStream = file.getInputStream();

        if (file.getSize() > MAX_FILE_SIZE) {
            processedImage = compressImage(fileInputStream, file.getContentType());
        }

        return processedImage;
    }

    private static void validateFileType(MultipartFile file) {
        if (!VALID_IMAGE_TYPES.contains(file.getContentType())) {
            throw new IllegalArgumentException("Invalid file type: " + file.getContentType());
        }
    }

    private static ProcessedImage compressImage(InputStream inputStream, String contentType) throws IOException {
        BufferedImage image = ImageIO.read(inputStream);
        if (image == null) {
            throw new IllegalArgumentException("The provided file is not a valid image.");
        }

        // Convert to JPEG for compression if not already in JPEG format
        if (!"image/jpeg".equals(contentType)) {
            image = convertToJPEG(image);
            contentType = "image/jpeg";
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        float compressionQuality = 0.85f; // Start with high quality
        boolean isCompressed = false;

        while (!isCompressed && compressionQuality > 0.1f) {
            baos.reset();
            Iterator<ImageWriter> writers = ImageIO.getImageWritersByMIMEType(contentType);
            if (!writers.hasNext()) {
                throw new IllegalStateException("No writer found for MIME type: " + contentType);
            }

            ImageWriter imageWriter = writers.next();
            try (ImageOutputStream ios = ImageIO.createImageOutputStream(baos)) {
                imageWriter.setOutput(ios);
                ImageWriteParam writeParams = imageWriter.getDefaultWriteParam();
                if (writeParams.canWriteCompressed()) {
                    writeParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                    writeParams.setCompressionQuality(compressionQuality);
                }

                imageWriter.write(null, new IIOImage(image, null, null), writeParams);
            } finally {
                imageWriter.dispose();
            }

            if (baos.size() <= MAX_FILE_SIZE) {
                isCompressed = true;
            } else {
                compressionQuality -= 0.05f;
            }
        }

        if (!isCompressed) {
            throw new IOException("Unable to compress image to the desired file size.");
        }

        return new ProcessedImage(new ByteArrayInputStream(baos.toByteArray()),baos.size());
    }

    private static BufferedImage convertToJPEG(BufferedImage image) {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = newImage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return newImage;
    }

    private static BufferedImage scaleImage(BufferedImage image, String contentType) {
        float scaleRatio = 1.0f;
        if ("image/png".equals(contentType)) {
            scaleRatio = 0.9f;
        }
        int width = (int) (image.getWidth() * scaleRatio);
        int height = (int) (image.getHeight() * scaleRatio);
        Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage bufferedScaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bufferedScaledImage.createGraphics();
        g.drawImage(scaledImage, 0, 0, null);
        g.dispose();
        return bufferedScaledImage;
    }
}

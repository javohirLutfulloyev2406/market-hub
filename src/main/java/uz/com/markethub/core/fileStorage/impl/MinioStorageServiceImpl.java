package uz.com.markethub.core.fileStorage.impl;


import io.minio.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.com.markethub.core.fileStorage.MinioStorageService;
import uz.com.markethub.core.config.MinioClientConfig;

import java.io.ByteArrayInputStream;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class MinioStorageServiceImpl implements MinioStorageService {

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucketName;

    @Value("${minio.expiry-seconds}")
    private int expirySeconds;

    @Override
    public String upload(String fileName, MultipartFile file) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            return fileName;
        } catch (Exception e) {
            throw new RuntimeException("Minio upload failed for " + fileName, e);
        }
    }

    @Override
    public String upload(String fileName, byte[] file) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(file)) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(inputStream, file.length, -1)
                            .contentType("application/octet-stream")
                            .build()
            );
            return fileName;
        } catch (Exception e) {
            throw new RuntimeException("Minio upload failed for " + fileName, e);
        }
    }

    @Override
    public byte[] download(String filePath) {
        try (GetObjectResponse response = minioClient.getObject(
                GetObjectArgs.builder().bucket(bucketName).object(filePath).build())) {
            return response.readAllBytes();
        } catch (Exception e) {
            throw new RuntimeException("Failed to download " + filePath, e);
        }
    }

    @Override
    public void delete(String filePath) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder().bucket(bucketName).object(filePath).build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete " + filePath, e);
        }
    }

    @Override
    public String generatePresignedUrl(String filePath) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(filePath)
                            .expiry(expirySeconds, TimeUnit.SECONDS)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate presigned URL", e);
        }
    }
}

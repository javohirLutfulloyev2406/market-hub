package uz.com.markethub.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public record MinioConfig(String url, String consoleUrl, String accessKey, String secretKey, String bucketName) {
    public MinioConfig(@Value("${minio.url}") String url,
                       @Value("${minio.console-url}") String consoleUrl,
                       @Value("${minio.access-key}") String accessKey,
                       @Value("${minio.secret-key}") String secretKey,
                       @Value("${minio.bucket}") String bucketName) {
        this.url = url;
        this.consoleUrl = consoleUrl;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.bucketName = bucketName;
    }
}

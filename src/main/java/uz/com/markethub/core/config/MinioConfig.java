package uz.com.markethub.core.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "minio")
public class MinioConfig {
    private String url;
    private String consoleUrl;
    private String accessKey;
    private String secretKey;
    private String bucketName;
}
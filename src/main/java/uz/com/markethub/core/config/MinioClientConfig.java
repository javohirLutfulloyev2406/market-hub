//package uz.com.markethub.core.config;
//
//
//import io.minio.MinioClient;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class MinioClientConfig {
//
//    private final MinioConfig minioConfig;
//
//    public MinioClientConfig(MinioConfig minioConfig) {
//        this.minioConfig = minioConfig;
//    }
//    @Bean11

//    public MinioClient minioClient() {
//        return MinioClient.builder()
//                .endpoint(minioConfig.url())
//                .credentials(minioConfig.accessKey(), minioConfig.secretKey())
//                .build();
//    }
//}

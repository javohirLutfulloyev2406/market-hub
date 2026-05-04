package uz.com.markethub.core.fileStorage;

import org.springframework.web.multipart.MultipartFile;

public interface MinioStorageService {
    String upload(String fileName, MultipartFile file);
    String upload(String fileName, byte [] file);
    byte[] download(String filePath);
    void delete(String filePath);
    String generatePresignedUrl(String filePath);
}

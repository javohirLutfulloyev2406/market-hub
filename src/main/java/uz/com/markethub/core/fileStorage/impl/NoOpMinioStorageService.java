package uz.com.markethub.core.fileStorage.impl;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.com.markethub.core.fileStorage.MinioStorageService;

@Service
@ConditionalOnMissingBean(MinioStorageService.class)
public class NoOpMinioStorageService implements MinioStorageService {

    private static final String MSG = "Minio is not configured. Set 'minio.url' to enable file storage.";

    @Override
    public String upload(String fileName, MultipartFile file) {
        throw new UnsupportedOperationException(MSG);
    }

    @Override
    public String upload(String fileName, byte[] file) {
        throw new UnsupportedOperationException(MSG);
    }

    @Override
    public byte[] download(String filePath) {
        throw new UnsupportedOperationException(MSG);
    }

    @Override
    public void delete(String filePath) {
        throw new UnsupportedOperationException(MSG);
    }

    @Override
    public String generatePresignedUrl(String filePath) {
        throw new UnsupportedOperationException(MSG);
    }
}
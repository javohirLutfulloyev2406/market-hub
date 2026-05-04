package uz.com.markethub.module.fileSystem.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import uz.com.markethub.core.enums.FileExtension;
import uz.com.markethub.core.enums.FilePrefix;
import uz.com.markethub.module.fileSystem.domain.FileEntity;
import uz.com.markethub.module.fileSystem.dto.FileDTO;


import java.util.List;

public interface FileService {
    List<FileDTO.Full> uploadFiles(Long logId, List<MultipartFile> file);

    FileEntity uploadFile(Long logId, FilePrefix filePrefix, FileExtension fileExtension, byte[] file);

    FileDTO.Full findById(Long logId, Long id);

    FileEntity findEntityById(Long logId, Long id);

    void deleteById(Long logId, Long id);

    boolean existsById(Long logId, Long id);

    List<FileDTO> findAll(Long logId);

    Page<FileDTO.Full> findAllPaged(Long logId, MultiValueMap<String, String> filters, Pageable pageable);

    List<FileDTO> findFilesByIds(Long logId, List<Long> ids);

    List<FileEntity> findEntitiesByIds(Long logId, List<Long> ids);

}

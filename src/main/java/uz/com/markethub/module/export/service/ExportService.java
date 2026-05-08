package uz.com.markethub.module.export.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.com.markethub.module.export.constants.ExportStatus;
import uz.com.markethub.module.export.domain.ExportEntity;
import uz.com.markethub.module.export.dto.ExportDTO;
import uz.com.markethub.module.fileSystem.domain.FileEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface ExportService {

    ExportDTO.Full create(Long logId, ExportDTO dto);

    List<ExportDTO.Full> findAll(Long logId);

    Page<ExportDTO.Full> findAllPaged(Long logId, Pageable pageable);

    ExportDTO.Full findById(Long logId, Long id);

    ExportEntity findEntityById(Long logId, Long id);

    ExportDTO.Full update(Long logId, Long id, ExportDTO dto);

    void setParams(Long logId, Long id, FileEntity fileEntity, ExportStatus exportStatus, LocalDateTime completedAt, String errorMessage);

    void deleteById(Long logId, Long id);

    boolean existsById(Long logId, Long id);
}

package uz.com.markethub.module.export.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.com.markethub.core.exception.ResourceNotFoundException;
import uz.com.markethub.core.fileStorage.MinioStorageService;
import uz.com.markethub.core.record.ApiStatus;
import uz.com.markethub.module.User.service.UserService;
import uz.com.markethub.module.export.constants.ExportStatus;
import uz.com.markethub.module.export.domain.ExportEntity;
import uz.com.markethub.module.export.dto.ExportDTO;
import uz.com.markethub.module.export.repository.ExportRepository;
import uz.com.markethub.module.export.service.ExportService;
import uz.com.markethub.module.fileSystem.domain.FileEntity;
import uz.com.markethub.security.util.SecurityUtils;

import java.time.LocalDateTime;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class ExportServiceImpl implements ExportService {

    private final ExportRepository repository;
    private final MinioStorageService storageService;
    private final UserService userService;

    @Override
    @Transactional
    public ExportDTO.Full create(Long logId, ExportDTO dto) {
        log.debug("Request -- logId: {}, create data: {}", logId, dto);

        ExportEntity entity = dto.map2Entity(userService.findEntityById(logId, SecurityUtils.getAuthenticatedUserId()));
        ExportEntity savedEntity = repository.save(entity);
        ExportDTO.Full fullDTO = savedEntity.map2FullDTO(storageService);

        log.debug("Response -- logId: {}, data: {}", logId, fullDTO);
        return fullDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExportDTO.Full> findAll(Long logId) {
        log.debug("Request -- logId: {}, findAll", logId);

        List<ExportDTO.Full> dtoList = repository.findAllByUser(userService.findEntityById(logId, SecurityUtils.getAuthenticatedUserId()))
                .stream()
                .map(entity -> entity.map2FullDTO(storageService))
                .toList();

        log.debug("Response -- logId: {}, data: {}", logId, dtoList);
        return dtoList;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExportDTO.Full> findAllPaged(Long logId, Pageable pageable) {
        log.debug("Request -- logId: {}, findAllPaged pageable: {}", logId, pageable);

        Page<ExportDTO.Full> pages = repository.findAllByUser(userService.findEntityById(logId, SecurityUtils.getAuthenticatedUserId()), pageable)
                .map(entity -> entity.map2FullDTO(storageService));

        log.debug("Response -- logId: {}, data: {}", logId, pages);
        return pages;
    }

    @Override
    @Transactional(readOnly = true)
    public ExportDTO.Full findById(Long logId, Long id) {
        log.debug("Request -- logId: {}, findBy id: {}", logId, id);

        ExportEntity entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(logId, id, ApiStatus.ERR_ID_NOT_FOUND));

        ExportDTO.Full fullDTO = entity.map2FullDTO(storageService);

        log.debug("Response -- logId: {}, data: {}", logId, fullDTO);
        return fullDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public ExportEntity findEntityById(Long logId, Long id) {
        log.debug("Request -- logId: {}, findEntityBy id: {}", logId, id);

        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(logId, id, ApiStatus.ERR_ID_NOT_FOUND));
    }

    @Override
    @Transactional
    public ExportDTO.Full update(Long logId, Long id, ExportDTO dto) {
        log.debug("Request -- logId: {}, update id: {}, data: {}", logId, id, dto);

        ExportEntity entity = findEntityById(logId, id);
        dto.set2Entity(entity);
        ExportEntity savedEntity = repository.save(entity);

        ExportDTO.Full fullDTO = savedEntity.map2FullDTO(storageService);

        log.debug("Response -- logId: {}, data: {}", logId, fullDTO);
        return fullDTO;
    }

    @Override
    @Transactional
    public void setParams(Long logId, Long id, FileEntity fileEntity, ExportStatus exportStatus,
                          LocalDateTime completedAt, String errorMessage) {

        log.debug("Request -- logId: {}, setParams id: {}, exportStatus: {}, completedAt: {}",
                logId, id, exportStatus, completedAt);

        ExportEntity entity = findEntityById(logId, id);
        entity.setFile(fileEntity);
        entity.setStatus(exportStatus);
        entity.setCompletedAt(completedAt);
        entity.setErrorMessage(errorMessage);

        repository.save(entity);

        log.debug("Response -- logId: {}, status updated successfully", logId);
    }

    @Override
    @Transactional
    public void deleteById(Long logId, Long id) {
        log.debug("Request -- logId: {}, deleteBy id: {}", logId, id);

        repository.deleteById(id);

        log.debug("Response -- logId: {}, entity deleted successfully", logId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long logId, Long id) {
        log.debug("Request -- logId: {}, existsBy id: {}", logId, id);

        boolean exists = repository.existsById(id);

        log.debug("Response -- logId: {}, exists: {}", logId, exists);
        return exists;
    }
}

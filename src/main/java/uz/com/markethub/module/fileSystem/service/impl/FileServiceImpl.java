package uz.com.markethub.module.fileSystem.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import uz.com.markethub.core.enums.FileExtension;
import uz.com.markethub.core.enums.FilePrefix;
import uz.com.markethub.core.exception.ResourceNotFoundException;
import uz.com.markethub.core.fileStorage.MinioStorageService;
import uz.com.markethub.core.record.ApiStatus;
import uz.com.markethub.core.specification.GenericSpecifications;
import uz.com.markethub.core.util.HelperUtil;
import uz.com.markethub.module.fileSystem.domain.FileEntity;
import uz.com.markethub.module.fileSystem.dto.FileDTO;
import uz.com.markethub.module.fileSystem.repository.FileRepository;
import uz.com.markethub.module.fileSystem.service.FileService;


import java.nio.file.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository repository;
    private final MinioStorageService storageService;

    @Transactional
    public List<FileDTO.Full> uploadFiles(Long logId, List<MultipartFile> files) {
        log.debug("Request -- logId: {}, upload files count: {}", logId, files.size());

        return files.stream().map(file -> {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

            if (fileName.contains("..")) {
                throw new RuntimeException("Invalid path sequence: " + fileName);
            }

            String filePath = storageService.upload(fileName, file);

            FileEntity entity = FileEntity.builder()
                    .fileName(fileName)
                    .filePath(filePath)
                    .fileSize(file.getSize())
                    .contentType(file.getContentType())
                    .build();

            FileDTO.Full dto = repository.save(entity).map2FullDTO(storageService);
            log.debug("Response -- logId: {}, saved file: {}", logId, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public FileEntity uploadFile(Long logId, FilePrefix filePrefix, FileExtension fileExtension, byte[] file) {

        String fileName = HelperUtil.generateExportFileName(filePrefix.getPrefix(), fileExtension.getExtension());

        String filePath = storageService.upload(fileName, file);

        FileEntity entity = FileEntity.builder()
                .fileName(fileName)
                .filePath(filePath)
                .fileSize((long) file.length)
                .contentType(fileExtension.getExtension())
                .build();

        FileEntity saved = repository.save(entity);

        log.debug("Response -- logId: {}, saved file: {}", logId, saved);
        return saved;
    }


    @Override
    @Transactional(readOnly = true)
    public FileDTO.Full findById(Long logId, Long id) {
        log.debug("Request -- logId: {}, findById: {}", logId, id);

        FileDTO.Full dto = repository.findById(id)
                .map(entity -> entity.map2FullDTO(storageService))
                .orElseThrow(() -> new ResourceNotFoundException(logId, id, ApiStatus.ERR_ID_NOT_FOUND));

        log.debug("Response -- logId: {}, data: {}", logId, dto);
        return dto;
    }


    @Override
    public FileEntity findEntityById(Long logId, Long id) {
        log.debug("Request -- logId: {}, findEntityById: {}", logId, id);

        FileEntity entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(logId, id, ApiStatus.ERR_ID_NOT_FOUND));

        log.debug("Response -- logId: {}, data: {}", logId, entity);
        return entity;
    }

    @Override
    @Transactional
    public void deleteById(Long logId, Long id) {
        log.debug("Request -- logId: {}, deleteById: {}", logId, id);

        FileEntity entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(logId, id, ApiStatus.ERR_ID_NOT_FOUND));

        storageService.delete(entity.getFilePath());

        repository.deleteById(id);

        log.debug("Response -- logId: {}, deleted successfully", logId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long logId, Long id) {
        log.debug("Request -- logId: {}, existsById: {}", logId, id);
        boolean exists = repository.existsById(id);
        log.debug("Response -- logId: {}, data: {}", logId, exists);
        return exists;
    }

    @Override
    @Transactional(readOnly = true)
    public List<FileDTO> findAll(Long logId) {
        log.debug("Request -- logId: {}, findAll", logId);

        List<FileDTO> dtoList = repository.findAll()
                .stream()
                .map(FileEntity::map2DTO)
                .toList();

        log.debug("Response -- logId: {}, data: {}", logId, dtoList);
        return dtoList;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FileDTO.Full> findAllPaged(Long logId, MultiValueMap<String, String> filters, Pageable pageable) {

        Page<FileDTO.Full> pages = repository.findAll(
                        GenericSpecifications.byFilters(filters.toSingleValueMap()),
                        pageable)
                .map(fileEntity -> fileEntity.map2FullDTO(storageService));

        log.debug("Response -- logId: {}, data: {}", logId, pages);
        return pages;
    }

    @Override
    @Transactional(readOnly = true)
    public List<FileDTO> findFilesByIds(Long logId, List<Long> ids) {
        log.debug("Request -- logId: {}, findFilesByIds: {}", logId, ids);

        List<FileDTO> dtoList = repository.findAllById(ids)
                .stream()
                .map(FileEntity::map2DTO)
                .toList();

        log.debug("Response -- logId: {}, data: {}", logId, dtoList);
        return dtoList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<FileEntity> findEntitiesByIds(Long logId, List<Long> ids) {
        log.debug("Request -- logId: {}, findFileEntitiesByIds: {}", logId, ids);

        List<FileEntity> entities = repository.findAllById(ids);

        log.debug("Response -- logId: {}, data: {}", logId, entities);
        return entities;
    }
}

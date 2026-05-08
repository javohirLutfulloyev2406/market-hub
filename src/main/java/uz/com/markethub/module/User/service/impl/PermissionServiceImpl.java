package uz.com.markethub.module.User.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Sort;
import org.springframework.util.MultiValueMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.com.markethub.core.exception.ResourceNotFoundException;
import uz.com.markethub.core.record.ApiStatus;
import uz.com.markethub.core.specification.GenericSpecifications;
import uz.com.markethub.module.User.domain.PermissionEntity;
import uz.com.markethub.module.User.dto.PermissionDTO;
import uz.com.markethub.module.User.repository.PermissionRepository;
import uz.com.markethub.module.User.service.PermissionService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository repository;

    @Override
    public PermissionDTO.Full create(Long logId, PermissionDTO dto) {
        log.debug("Request -- logId: {}, create data: {}", logId, dto);

        PermissionDTO.Full fullDTO = repository
                .save(dto.map2Entity())
                .map2FullDTO();

        log.debug("Response -- logId: {}, data: {}", logId, fullDTO);
        return fullDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PermissionDTO.Full> findAllPaged(Long logId, MultiValueMap<String, String> filters, Pageable pageable) {
        log.debug("Request -- logId: {}, findAllBy pageable: {}", logId, pageable);

        Page<PermissionDTO.Full> pages = repository.findAll(
                        GenericSpecifications.byFilters(filters.toSingleValueMap()),
                        pageable)
                .map(PermissionEntity::map2FullDTO);

        log.debug("Response -- logId: {}, data: {}", logId, pages);
        return pages;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PermissionDTO> findAllShortInfo(Long logId, MultiValueMap<String, String> filters) {
        log.debug("Request -- logId: {}, findAllShortInfo", logId);

        List<PermissionDTO> fullDTOS = repository
                .findAll(GenericSpecifications.byFilters(filters.toSingleValueMap()),  Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream()
                .map(PermissionEntity::map2DTO)
                .collect(Collectors.toList());

        log.debug("Response -- logId: {}, data: {}", logId, fullDTOS);
        return fullDTOS;
    }

    @Override
    @Transactional(readOnly = true)
    public PermissionDTO.Full findById(Long logId, Long id) {
        log.debug("Request -- logId: {}, finBy id: {}", logId, id);

        PermissionDTO.Full fullDTO = repository
                .findById(id)
                .map(PermissionEntity::map2FullDTO)
                .orElseThrow(() -> new ResourceNotFoundException(logId, id, ApiStatus.ERR_ID_NOT_FOUND));

        log.debug("Response -- logId: {}, data: {}", logId, fullDTO);
        return fullDTO;
    }

    @Override
    public PermissionEntity findEntityById(Long logId, Long id) {
        log.debug("Request -- logId: {}, findEntityBy id: {}", logId, id);

        PermissionEntity entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(logId, id, ApiStatus.ERR_ID_NOT_FOUND));

        log.debug("Response -- logId: {}, data: {}", logId, entity);
        return entity;
    }

    @Override
    public PermissionDTO.Full update(Long logId, Long id, PermissionDTO.Full dto) {
        log.debug("Request -- logId: {}, update data: {}", logId, dto);

        PermissionEntity entity = findEntityById(logId, id);

        dto.set2Entity(entity);

        PermissionDTO.Full fullDTO = repository
                .save(entity)
                .map2FullDTO();

        log.debug("Response -- logId: {}, data: {}", logId, fullDTO);
        return fullDTO;
    }

    @Override
    public void deleteById(Long logId, Long id) {
        log.debug("Request -- logId: {}, deleteBy id: {}", logId, id);

        PermissionEntity entity = findEntityById(logId, id);
        delete(logId, entity);
    }

    @Override
    public void delete(Long logId, PermissionEntity entity) {
        log.debug("Request -- logId: {}, delete data: {}", logId, entity);

        entity.setDeleted(true);
        repository.save(entity);
    }

    @Override
    public boolean existsById(Long logId, Long id) {
        log.debug("Request -- logId: {}, existBy id: {}", logId, id);

        boolean exists = repository.existsById(id);

        log.debug("Response -- logId: {}, data: {}", logId, exists);
        return exists;
    }

    @Override
    public Set<PermissionEntity> findAllByPermissionIds(Long logId, List<Long> permissionIds) {
        log.debug("Request -- logId: {}, update data: {}", logId, permissionIds);

        List<PermissionEntity> allById = repository.findAllByIdIn(permissionIds);

        if (allById.isEmpty()) {
            throw new ResourceNotFoundException(logId, permissionIds, ApiStatus.ERR_ID_NOT_FOUND);
        }
        Set<PermissionEntity> permissionEntities = new HashSet<>(allById);

        log.debug("Response -- logId: {}, data: {}", logId, permissionEntities);
        return permissionEntities;
    }
}

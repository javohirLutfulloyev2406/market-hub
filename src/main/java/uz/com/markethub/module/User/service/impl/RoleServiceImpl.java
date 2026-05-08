package uz.com.markethub.module.User.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import uz.com.markethub.core.exception.ResourceNotFoundException;
import uz.com.markethub.core.record.ApiStatus;
import uz.com.markethub.core.specification.GenericSpecifications;
import uz.com.markethub.module.User.domain.RoleEntity;
import uz.com.markethub.module.User.dto.RoleDTO;
import uz.com.markethub.module.User.repository.RoleRepository;
import uz.com.markethub.module.User.service.PermissionService;
import uz.com.markethub.module.User.service.RoleService;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository repository;
    private final PermissionService permissionService;

    @Override
    public RoleDTO.Full create(Long logId, RoleDTO.CreateOrUpdate dto) {
        log.debug("Request -- logId: {}, create data: {}", logId, dto);

        RoleDTO.Full fullDTO = repository
                .save(dto.map2Entity(permissionService.findAllByPermissionIds(logId, dto.getPermissionIds())))
                .map2FullDTO();

        log.debug("Response -- logId: {}, data: {}", logId, fullDTO);
        return fullDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RoleDTO.Full> findAllPaged(Long logId, MultiValueMap<String, String> filters, Pageable pageable) {
        log.debug("Request -- logId: {}, findAllBy pageable: {}", logId, pageable);

        Page<RoleDTO.Full> pages = repository.findAll(
                        GenericSpecifications.byFilters(filters.toSingleValueMap()),
                        pageable)
                .map(RoleEntity::map2FullDTO);

        log.debug("Response -- logId: {}, data: {}", logId, pages);
        return pages;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleDTO> findAllShortInfo(Long logId) {
        log.debug("Request -- logId: {}, findAllShortInfo", logId);

        List<RoleDTO> fullDTOS = repository
                .findAll()
                .stream()
                .map(RoleEntity::map2DTO)
                .collect(Collectors.toList());

        log.debug("Response -- logId: {}, data: {}", logId, fullDTOS);
        return fullDTOS;
    }

    @Override
    @Transactional(readOnly = true)
    public RoleDTO.Full findById(Long logId, Long id) {
        log.debug("Request -- logId: {}, finBy id: {}", logId, id);

        RoleDTO.Full fullDTO = repository
                .findById(id)
                .map(RoleEntity::map2FullDTO)
                .orElseThrow(() -> new ResourceNotFoundException(logId, id, ApiStatus.ERR_ID_NOT_FOUND));

        log.debug("Response -- logId: {}, data: {}", logId, fullDTO);
        return fullDTO;
    }

    @Override
    public RoleEntity findEntityById(Long logId, Long id) {
        log.debug("Request -- logId: {}, findEntityBy id: {}", logId, id);

        RoleEntity entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(logId, id, ApiStatus.ERR_ID_NOT_FOUND));

        log.debug("Response -- logId: {}, data: {}", logId, entity);
        return entity;
    }

    @Override
    @Transactional
    public RoleDTO.Full update(Long logId, Long id, RoleDTO.CreateOrUpdate dto) {
        log.debug("Request -- logId: {}, update data: {}", logId, dto);

        RoleEntity entity = findEntityById(logId, id);

        dto.set2Entity(entity);

        RoleDTO.Full fullDTO = repository
                .save(entity)
                .map2FullDTO();

        log.debug("Response -- logId: {}, data: {}", logId, fullDTO);
        return fullDTO;
    }

    @Override
    @Transactional
    public void deleteById(Long logId, Long id) {
        log.debug("Request -- logId: {}, deleteBy id: {}", logId, id);

        RoleEntity entity = findEntityById(logId, id);
        delete(logId, entity);
    }

    @Override
    @Transactional
    public void delete(Long logId, RoleEntity entity) {
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
    @Transactional(readOnly = true)
    public List<RoleEntity> findEntitiesByIds(Long logId, List<Long> ids) {
        log.debug("Request -- logId: {}, findFileEntitiesByIds: {}", logId, ids);

        List<RoleEntity> entities = repository.findAllById(ids);

        log.debug("Response -- logId: {}, data: {}", logId, entities);
        return entities;
    }
}

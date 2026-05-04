package uz.com.markethub.module.settings.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import uz.com.markethub.core.exeption.ResourceNotFoundException;
import uz.com.markethub.core.record.ApiStatus;
import uz.com.markethub.core.specification.GenericSpecifications;
import uz.com.markethub.module.settings.domain.ApiMessageEntity;
import uz.com.markethub.module.settings.dto.ApiMessageDTO;
import uz.com.markethub.module.settings.repository.ApiMessageRepository;
import uz.com.markethub.module.settings.service.ApiMessageService;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class ApiMessageServiceImpl implements ApiMessageService {

    private final ApiMessageRepository repository;


    @Override
    @Transactional
    public ApiMessageDTO.Full create(Long logId, ApiMessageDTO dto) {
        log.debug("Request -- logId: {}, create data: {}", logId, dto);

        ApiMessageDTO.Full fullDTO = repository
                .save(dto.map2Entity())
                .map2FullDTO();

        log.debug("Response -- logId: {}, data: {}", logId, fullDTO);
        return fullDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApiMessageDTO.Full> findAll(Long logId) {
        log.debug("Request -- logId: {}, findAll", logId);

        List<ApiMessageDTO.Full> dtoList = repository
                .findAll()
                .stream()
                .map(ApiMessageEntity::map2FullDTO)
                .toList();
        log.debug("Response -- logId: {},data: {}", logId, dtoList);
        return dtoList;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ApiMessageDTO.Full> findAllPaged(Long logId, MultiValueMap<String, String> filters, Pageable pageable) {
        log.debug("Request -- logId: {}, findAllPagedBy pageable: {}", logId, pageable);

        Page<ApiMessageDTO.Full> pages = repository.findAll(
                        GenericSpecifications.byFilters(filters.toSingleValueMap()),
                        pageable)
                .map(ApiMessageEntity::map2FullDTO);

        log.debug("Response -- logId: {}, data: {}", logId, pages);
        return pages;
    }

    @Override
    @Transactional(readOnly = true)
    public ApiMessageDTO.Full findById(Long logId, Long id) {
        log.debug("Request -- logId: {}, findBy id: {}", logId, id);

        ApiMessageDTO.Full fullDTO = repository
                .findById(id)
                .map(ApiMessageEntity::map2FullDTO)
                .orElseThrow(() -> new ResourceNotFoundException(logId, id, ApiStatus.ERR_ID_NOT_FOUND));

        log.debug("Response -- logId: {}, data: {}", logId, fullDTO);
        return fullDTO;
    }

    @Override
    public ApiMessageEntity findEntityById(Long logId, Long id) {
        log.debug("Request -- logId: {}, findByEntityBy id: {}", logId, id);

        ApiMessageEntity entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(logId, id, ApiStatus.ERR_ID_NOT_FOUND));

        log.debug("Response -- logId: {},data: {}", logId, entity);
        return entity;

    }

    @Override
    @Transactional(readOnly = true)
    public ApiMessageDTO findByKey(Long logId, String key) {
        log.debug("Request -- logId: {}, findBy key: {}",logId,key);

        ApiMessageDTO dto = repository
                .findByKey(key)
                .map(ApiMessageEntity::map2DTO)
                .orElseThrow();

        log.debug("Response -- logId: {}, data: {}",logId,dto);
        return dto;
    }

    @Override
    @Transactional
    public ApiMessageDTO.Full update(Long logId, Long id, ApiMessageDTO dto) {
        log.debug("Request -- logId: {}, update id: {},data: {}",logId,id,dto);

        ApiMessageEntity entity = findEntityById(logId, id);

        dto.set2Entity(entity);

        ApiMessageDTO.Full full = repository.save(entity).map2FullDTO();
        return full;
    }

    @Override
    @Transactional
    public void deleteById(Long logId, Long id) {
        log.debug("Request -- logId: {}, deleteBy id: {}", logId, id);

        repository.deleteById(id);
    }

    @Override
    public boolean existsById(Long logId, Long id) {
        log.debug("Request -- logId: {}, existBy id: {}", logId, id);

        boolean exists = repository.existsById(id);

        log.debug("Response -- logId: {}, data: {}", logId, exists);
        return exists;
    }


}

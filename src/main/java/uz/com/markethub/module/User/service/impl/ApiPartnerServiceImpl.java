package uz.com.markethub.module.User.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.MultiValueMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.com.markethub.core.exception.ResourceNotFoundException;
import uz.com.markethub.core.record.ApiStatus;
import uz.com.markethub.core.specification.GenericSpecifications;
import uz.com.markethub.core.util.HelperUtil;
import uz.com.markethub.module.User.domain.ApiPartnerEntity;
import uz.com.markethub.module.User.dto.ApiPartnerDTO;
import uz.com.markethub.module.User.record.PartnerInfoRecord;
import uz.com.markethub.module.User.repository.ApiPartnerRepository;
import uz.com.markethub.module.User.service.ApiPartnerService;
import uz.com.markethub.security.execption.ApiPartnerException;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class ApiPartnerServiceImpl implements ApiPartnerService {
    private final ApiPartnerRepository repository;

    @Override
    public ApiPartnerDTO.Full create(Long logId, ApiPartnerDTO.Full dto) {
        log.info("Request -- logId: {}, create data: {}", logId, dto);

        ApiPartnerDTO.Full fullDTO = repository
                .save(dto.map2Entity())
                .map2FullDTO();

        log.info("Response -- logId: {}, data: {}", logId, fullDTO);
        return fullDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ApiPartnerDTO.Full> findAllPaged(Long logId, MultiValueMap<String, String> filters, Pageable pageable) {
        log.info("Request -- logId: {}, findAllBy pageable: {}", logId, pageable);

        Page<ApiPartnerDTO.Full> pages = repository.findAll(
                        GenericSpecifications.byFilters(filters.toSingleValueMap()),
                        pageable)
                .map(ApiPartnerEntity::map2FullDTO);

        log.info("Response -- logId: {}, data: {}", logId, pages);
        return pages;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApiPartnerDTO> findAllShortInfo(Long logId) {
        log.info("Request -- logId: {}, findAllShortInfo", logId);

        List<ApiPartnerDTO> fullDTOS = repository
                .findAll()
                .stream()
                .map(ApiPartnerEntity::map2DTO)
                .collect(Collectors.toList());

        log.info("Response -- logId: {}, data: {}", logId, fullDTOS);
        return fullDTOS;
    }

    @Override
    @Transactional(readOnly = true)
    public ApiPartnerDTO.Full findById(Long logId, Long id) {
        log.info("Request -- logId: {}, finBy id: {}", logId, id);

        ApiPartnerDTO.Full fullDTO = repository
                .findById(id)
                .map(ApiPartnerEntity::map2FullDTO)
                .orElseThrow(() -> new ResourceNotFoundException(logId,id, ApiStatus.ERR_ID_NOT_FOUND));

        log.info("Response -- logId: {}, data: {}", logId, fullDTO);
        return fullDTO;
    }

    @Override
    public ApiPartnerDTO.Full findByCode(String code) {
        return repository
                .findByCode(code)
                .map(ApiPartnerEntity::map2FullDTO)
                .orElseThrow(() -> new ApiPartnerException(ApiStatus.ERR_API_PARTNER_VALIDATION));
    }

    @Override
    public ApiPartnerDTO.Full findByCode(Long logId, String code) {
        log.info("Request -- logId: {}, findByCode data: {}", logId, code);

        ApiPartnerDTO.Full fullDTO = repository
                .findByCode(code)
                .map(ApiPartnerEntity::map2FullDTO)
                .orElseThrow(() -> new ApiPartnerException(ApiStatus.ERR_API_PARTNER_VALIDATION));

        log.info("Response -- logId: {}, data: {}", logId, fullDTO);

        return fullDTO;
    }

    @Override
    public ApiPartnerEntity findEntityById(Long logId, Long id) {
        log.info("Request -- logId: {}, findEntityBy id: {}", logId, id);

        ApiPartnerEntity entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(logId,id, ApiStatus.ERR_ID_NOT_FOUND ));

        log.info("Response -- logId: {}, data: {}", logId, entity);
        return entity;
    }

    @Override
    public ApiPartnerDTO.Full update(Long logId, Long id, ApiPartnerDTO.Full dto) {
        log.info("Request -- logId: {}, update data: {}", logId, dto);

        ApiPartnerEntity entity = findEntityById(logId, id);

        dto.set2Entity(entity);

        ApiPartnerDTO.Full fullDTO = repository
                .save(entity)
                .map2FullDTO();

        log.info("Response -- logId: {}, data: {}", logId, fullDTO);
        return fullDTO;
    }

    @Override
    public void deleteById(Long logId, Long id) {
        log.info("Request -- logId: {}, deleteBy id: {}", logId, id);

        ApiPartnerEntity entity = findEntityById(logId, id);
        delete(logId, entity);
    }

    @Override
    public void delete(Long logId, ApiPartnerEntity entity) {
        log.info("Request -- logId: {}, delete data: {}", logId, entity);

        entity.setDeleted(true);
        repository.save(entity);
    }

    @Override
    public boolean existsById(Long logId, Long id) {
        log.info("Request -- logId: {}, existBy id: {}", logId, id);

        boolean exists = repository.existsById(id);

        log.info("Response -- logId: {}, data: {}", logId, exists);
        return exists;
    }

    @Override
    public void checkIsEnabledAndCorrectVersion(PartnerInfoRecord record) {
        ApiPartnerDTO.Full full = findByCode(record.code());

        checkIsEnabledAndCorrectVersion(full.isEnabled(), full.getVersion(), record.version());
    }

    @Override
    public void checkIsEnabledAndCorrectVersion(boolean isEnabled, String requiredVersion, String version) {
        if (!isEnabled) {
            throw new ApiPartnerException(ApiStatus.ERR_API_PARTNER_NOT_ENABLED);
        }
        if (!HelperUtil.versionCompare(requiredVersion, version)) {
            throw new ApiPartnerException(ApiStatus.ERR_API_PARTNER_VERSION_EXPIRED);
        }
    }
}

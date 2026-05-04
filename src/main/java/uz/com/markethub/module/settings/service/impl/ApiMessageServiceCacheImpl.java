package uz.com.markethub.module.settings.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import uz.com.markethub.core.exeption.ResourceNotFoundException;
import uz.com.markethub.core.record.ApiStatus;
import uz.com.markethub.core.util.HelperUtil;
import uz.com.markethub.module.settings.dto.ApiMessageDTO;
import uz.com.markethub.module.settings.service.ApiMessageService;
import uz.com.markethub.module.settings.service.cache.ApiMessageServiceCache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Log4j2
@Service
@RequiredArgsConstructor
public class ApiMessageServiceCacheImpl implements ApiMessageServiceCache {

    private final Map<String, ApiMessageDTO.Full> cache=new HashMap<>();
    private final ApiMessageService service;

    @PostConstruct
    public void initCache() {
        Long logId = HelperUtil.generateLogId();
        log.info("Init ApiMessage cache logId: {}",logId);

        List<ApiMessageDTO.Full> all = service.findAll(logId);

        all.forEach(dto->
                cache.put(dto.getKey(),dto)
        );

    }

    @Override
    public void create(Long logId, ApiMessageDTO.Full dto) {
        log.info("Request -- create logId: {}",logId);

        cache.put(dto.getKey(), dto);

    }

    @Override
    public ApiMessageDTO.Full findById(Long logId, Long id) {
        log.info("Request -- findById logId: {}, id: {}",logId,id);

        ApiMessageDTO.Full fullDTO = cache.values()
                .stream()
                .filter(full -> Objects.equals(full.getId(), id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(logId, id, ApiStatus.ERR_ID_NOT_FOUND));

        log.info("Response -- logId: {}, data: {}",logId,fullDTO);
        return fullDTO;
    }

    @Override
    public List<ApiMessageDTO.Full> findAll(Long logId) {
        log.info("Request -- findAll logId: {}",logId);

        List<ApiMessageDTO.Full> list = cache.values().stream().toList();

        log.info("Response -- logId: {}, data: {}",logId,list);
        return list;
    }

    @Override
    public Page<ApiMessageDTO.Full> findAllPaged(Long logId, MultiValueMap<String, String> filters, Pageable pageable) {

        log.info("Request -- findAll logId: {}", logId);

        List<ApiMessageDTO.Full> list = cache.values().stream().toList();

        Page<ApiMessageDTO.Full> pages = new PageImpl<>(list, pageable, list.size());

        log.info("Response -- logId: {}, data: {}", logId, pages);
        return pages;
    }

    @Override
    public ApiMessageDTO.Full findByKey(Long logId, String key) {
        log.info("Request -- findByKey logId: {}, key: {}", logId, key);

        if (!cache.containsKey(key)) {
            throw new ResourceNotFoundException(logId, key, ApiStatus.ERR_NOT_FOUND);
        }

        ApiMessageDTO.Full dto = cache.get(key);

        log.info("Response -- logId: {}, data: {}", logId, dto);
        return dto;
    }

    @Override
    public void update(Long logId, ApiMessageDTO.Full dto) {
        log.info("Request -- update logId: {}, data: {}", logId, dto);

        if (cache.containsKey(dto.getKey())) {
            if (cache.get(dto.getKey()).getUpdatedAt() < (dto.getUpdatedAt())) {
                cache.replace(dto.getKey(), dto);
            }
        }
    }

    @Override
    public void deleteById(Long logId, Long id) {
        log.info("Request -- deleteByKey logId: {}, id: {}", logId, id);

        ApiMessageDTO.Full dto = findById(logId, id);

        cache.remove(dto.getKey());
    }

    @Override
    public boolean existsById(Long logId, Long id) {
        log.info("Request -- existById logId: {}, id: {}", logId, logId);
        boolean exists = cache
                .values()
                .stream()
                .anyMatch(full -> Objects.equals(full.getId(), id));

        log.info("Response -- logId: {}, data: {}", logId, exists);
        return exists;
    }


}


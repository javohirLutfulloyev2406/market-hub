package uz.com.markethub.module.settings.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import uz.com.markethub.module.settings.dto.ApiMessageDTO;
import uz.com.markethub.module.settings.service.ApiMessageService;
import uz.com.markethub.module.settings.service.cache.ApiMessageServiceCache;
import uz.com.markethub.module.settings.service.facade.ApiMessageServiceFacade;


@Log4j2
@Service
@RequiredArgsConstructor
public class ApiMessageServiceFacadeImpl implements ApiMessageServiceFacade {

    private final ApiMessageService service;
    private final ApiMessageServiceCache serviceCache;

    @Override
    @Transactional
    public ApiMessageDTO.Full createEntityAndCache(Long logId, ApiMessageDTO dto) {
        log.debug("Request -- logId {}: create entity and cache data: {}",logId,dto);

        ApiMessageDTO.Full fullDto = service.create(logId, dto);
        serviceCache.create(logId,fullDto);

        log.debug("Response -- logId: {}, data: {}",logId,fullDto);
        return fullDto;
    }

    @Override
    @Transactional
    public ApiMessageDTO.Full updateEntityAndCache(Long logId, Long id, ApiMessageDTO.Full dto) {
        log.debug("Request -- logId: {}, update entity and cache id: {}, data: {}",logId,id,dto);

        ApiMessageDTO.Full update = service.update(logId, id, dto);
        serviceCache.update(logId,dto);

        log.debug("Response -- logId: {}, data: {}",logId,update);
        return update;
    }

    @Override
    @Transactional
    public void deleteEntityAndCacheId(Long logId, Long id) {
        log.debug("Request -- logId: {}, deleteById entity and cache id: {}, id: {}",logId,id,id);

        service.deleteById(logId,id);

        serviceCache.deleteById(logId,id);
    }
}

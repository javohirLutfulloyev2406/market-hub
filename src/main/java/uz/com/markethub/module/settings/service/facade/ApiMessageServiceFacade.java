package uz.com.markethub.module.settings.service.facade;


import uz.com.markethub.module.settings.dto.ApiMessageDTO;

public interface ApiMessageServiceFacade {

    ApiMessageDTO.Full createEntityAndCache(Long logId, ApiMessageDTO dto);

    ApiMessageDTO.Full updateEntityAndCache(Long logId,Long id,ApiMessageDTO.Full dto);

    void deleteEntityAndCacheId(Long logId,Long id);
}

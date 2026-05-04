package uz.com.markethub.module.settings.service.cache;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;
import uz.com.markethub.module.settings.dto.ApiMessageDTO;

import java.util.List;

public interface ApiMessageServiceCache {

    void initCache();

    void create(Long logId, ApiMessageDTO.Full dto);

    ApiMessageDTO.Full findById(Long logId,Long id);

    List<ApiMessageDTO.Full> findAll(Long logId);

    Page<ApiMessageDTO.Full> findAllPaged(Long logId, MultiValueMap<String, String> filters, Pageable pageable);

    ApiMessageDTO.Full findByKey(Long logId,String key);

    void update(Long logId,ApiMessageDTO.Full dto);

    void deleteById(Long logId,Long id);

    boolean existsById(Long logId,Long id);


}

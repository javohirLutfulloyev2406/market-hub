package uz.com.markethub.module.settings.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;
import uz.com.markethub.module.settings.domain.ApiMessageEntity;
import uz.com.markethub.module.settings.dto.ApiMessageDTO;


import java.util.List;

public interface ApiMessageService {

    ApiMessageDTO.Full create(Long logId, ApiMessageDTO dto);

    List<ApiMessageDTO.Full> findAll(Long logId);

    Page<ApiMessageDTO.Full> findAllPaged(Long logId, MultiValueMap<String, String> filters, Pageable pageable);

    ApiMessageDTO.Full findById(Long logId, Long id);

    ApiMessageEntity findEntityById(Long logId, Long id);

    ApiMessageDTO findByKey(Long logId, String key);

    ApiMessageDTO.Full update(Long logId, Long id, ApiMessageDTO dto);

    void deleteById(Long logId, Long id);

    boolean existsById(Long logId, Long id);
}

package uz.com.markethub.module.User.service;

import org.springframework.util.MultiValueMap;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.com.markethub.module.User.domain.ApiPartnerEntity;
import uz.com.markethub.module.User.dto.ApiPartnerDTO;
import uz.com.markethub.module.User.record.PartnerInfoRecord;

import java.util.List;

public interface ApiPartnerService {
    ApiPartnerDTO.Full create(Long logId, ApiPartnerDTO.Full dto);

    Page<ApiPartnerDTO.Full> findAllPaged(Long logId, MultiValueMap<String, String> filters, Pageable pageable);

    List<ApiPartnerDTO> findAllShortInfo(Long logId);

    ApiPartnerDTO.Full findById(Long logId, Long id);

    ApiPartnerDTO.Full findByCode(String code);

    ApiPartnerDTO.Full findByCode(Long logId, String code);

    ApiPartnerEntity findEntityById(Long logId, Long id);

    void deleteById(Long logId, Long id);

    void delete(Long logId, ApiPartnerEntity entity);

    boolean existsById(Long logId, Long id);

    ApiPartnerDTO.Full update(Long logId, Long id, ApiPartnerDTO.Full dto);

    void checkIsEnabledAndCorrectVersion(PartnerInfoRecord record);

    void checkIsEnabledAndCorrectVersion(boolean isEnabled, String requiredVersion, String version);


}

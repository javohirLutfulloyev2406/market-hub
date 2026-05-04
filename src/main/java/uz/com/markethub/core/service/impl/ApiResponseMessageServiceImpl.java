package uz.com.markethub.core.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.stereotype.Service;
import uz.com.markethub.core.enums.AcceptLanguage;
import uz.com.markethub.core.record.ApiResponseMessage;
import uz.com.markethub.core.record.ApiStatus;
import uz.com.markethub.core.service.ApiResponseMessageService;
import uz.com.markethub.module.User.util.HelperRequestUtil;
import uz.com.markethub.module.settings.dto.ApiMessageDTO;
import uz.com.markethub.module.settings.service.cache.ApiMessageServiceCache;


@Service
@RequiredArgsConstructor
@Log4j2
public class ApiResponseMessageServiceImpl implements ApiResponseMessageService {

    private final ApiMessageServiceCache apiMessageServiceCache;

    @Override
    public ApiResponseMessage findByApiStatus(Long logId, ApiStatus apiStatus) {
        log.info("Request -- logId: {}, findBy apiStatus: {}", logId, apiStatus);

        ApiResponseMessage result;

        try {
            ApiMessageDTO apiMessageDTO = apiMessageServiceCache.findByKey(logId, apiStatus.key());
            result = map2ApiResponseMessageDTO(apiMessageDTO);
        } catch (Exception e) {
            log.error("Error -- logId: {}, findBy apiStatus: {} , message:{}, ", logId, apiStatus, e.getMessage());
            result = apiStatus.map2ApiResponseMessage();
        }

        log.info("Response -- logId: {}, data: {}", logId, result);
        return result;
    }

    private ApiResponseMessage map2ApiResponseMessageDTO(ApiMessageDTO dto) {
        AcceptLanguage acceptLanguage = HelperRequestUtil.getAcceptLanguage();
        String message;

        if (acceptLanguage == AcceptLanguage.UZ) {
            message = dto.getMessageUz();
        } else if (acceptLanguage == AcceptLanguage.EN) {
            message = dto.getMessageEn();
        } else {
            message = dto.getMessageRu();
        }

        return new ApiResponseMessage(dto.getCode(), message);
    }
}

package uz.com.markethub.core.service;

import uz.com.markethub.core.record.ApiResponseMessage;
import uz.com.markethub.core.record.ApiStatus;

public interface ApiResponseMessageService {

    ApiResponseMessage findByApiStatus(Long logId, ApiStatus apiStatus);
}

package uz.com.markethub.core.exception;


import lombok.Getter;
import uz.com.markethub.core.record.ApiStatus;

@Getter
public class InvalidParameterException extends RuntimeException {
    private final Long logId;
    private final Object resource;
    private final ApiStatus apiStatus;

    public InvalidParameterException(Long logId, Object resource, ApiStatus apiStatus) {
        this.logId = logId;
        this.resource = resource;
        this.apiStatus = apiStatus;
    }

}
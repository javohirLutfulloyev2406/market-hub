package uz.com.markethub.core.exception;


import lombok.Getter;
import uz.com.markethub.core.record.ApiStatus;

@Getter
public class ResourceNotFoundException extends RuntimeException {
    private final Long logId;
    private final ApiStatus apiStatus;
    private final Object resource;

    public ResourceNotFoundException(Long logId, Object resource, ApiStatus apiStatus) {
        this.logId = logId;
        this.apiStatus = apiStatus;
        this.resource = resource;
    }
}
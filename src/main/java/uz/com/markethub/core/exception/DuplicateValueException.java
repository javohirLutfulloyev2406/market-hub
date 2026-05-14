package uz.com.markethub.core.exception;

import lombok.Getter;
import uz.com.markethub.core.record.ApiStatus;

@Getter
public class DuplicateValueException extends RuntimeException {
    private final Long logId;
    private final ApiStatus apiStatus;

    public DuplicateValueException(Long logId, ApiStatus apiStatus) {
        this.logId = logId;
        this.apiStatus = apiStatus;
    }
}
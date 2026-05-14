package uz.com.markethub.core.exception;

import lombok.Getter;
import uz.com.markethub.core.record.ApiStatus;

@Getter
public class ForbiddenException extends RuntimeException {
    private final Long logId;
    private final ApiStatus apiStatus;

    public ForbiddenException(Long logId, ApiStatus apiStatus) {
        this.logId = logId;
        this.apiStatus = apiStatus;
    }
}

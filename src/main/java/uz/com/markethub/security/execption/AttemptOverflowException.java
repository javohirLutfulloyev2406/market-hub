package uz.com.markethub.security.execption;

import lombok.Getter;
import uz.com.markethub.core.record.ApiStatus;

@Getter
public class AttemptOverflowException extends RuntimeException {
    private final ApiStatus status;

    public AttemptOverflowException(ApiStatus status) {
        this.status = status;
    }
}

package uz.com.markethub.security.execption;

import lombok.Getter;
import uz.com.markethub.core.record.ApiStatus;


@Getter
public class ApiPartnerException extends RuntimeException {
    private final ApiStatus status;

    public ApiPartnerException(ApiStatus status) {
        this.status = status;
    }
}

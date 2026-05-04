package uz.com.markethub.security.execption;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import uz.com.markethub.core.dto.ApiResponse;
import uz.com.markethub.core.record.ApiStatus;
import uz.com.markethub.core.service.ApiResponseMessageService;
import uz.com.markethub.core.util.HelperUtil;

@Log4j2
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class SecurityExceptionController {
    private final ApiResponseMessageService responseMessageService;

    @ExceptionHandler(value = AuthenticationException.class)
    public ResponseEntity<ApiResponse<?>> authenticationException(AuthenticationException exception) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        Long logId = HelperUtil.generateLogId();

        ResponseEntity<ApiResponse<?>> response = ResponseEntity
                .status(status)
                .body(new ApiResponse<>(
                                responseMessageService.findByApiStatus(logId, ApiStatus.ERR_UNAUTHORIZED)
                        )
                );

        log.error("Response -- data: {} ", response);
        return response;
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ApiResponse<?>> authenticationException(AccessDeniedException exception) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        Long logId = HelperUtil.generateLogId();

        ResponseEntity<ApiResponse<?>> response = ResponseEntity
                .status(status)
                .body(new ApiResponse<>(
                                responseMessageService.findByApiStatus(logId, ApiStatus.ERR_FORBIDDEN)
                        )
                );

        log.error("Response -- data: {} ", response);
        return response;
    }

    @ExceptionHandler(value = ApiPartnerException.class)
    public ResponseEntity<ApiResponse<?>> apiPartnerException(ApiPartnerException exception) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        Long logId = HelperUtil.generateLogId();

        ResponseEntity<ApiResponse<?>> response = ResponseEntity
                .status(status)
                .body(new ApiResponse<>(
                                responseMessageService.findByApiStatus(logId, exception.getStatus())
                        )
                );

        log.error("Response -- data: {} ", response);
        return response;
    }

    @ExceptionHandler(value = AttemptOverflowException.class)
    public ResponseEntity<ApiResponse<?>> attempOverflowException(AttemptOverflowException exception){
        HttpStatus status = HttpStatus.TOO_MANY_REQUESTS;
        Long logId = HelperUtil.generateLogId();

        ResponseEntity<ApiResponse<?>> response = ResponseEntity
                .status(status)
                .body(new ApiResponse<>(
                                responseMessageService.findByApiStatus(logId, exception.getStatus())
                        )
                );

        log.error("Response -- data: {} ", response);
        return response;
    }
}

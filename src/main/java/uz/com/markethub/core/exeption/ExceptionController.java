package uz.com.markethub.core.exeption;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import uz.com.markethub.core.dto.ApiResponse;
import uz.com.markethub.core.record.ApiResponseMessage;
import uz.com.markethub.core.record.ApiStatus;
import uz.com.markethub.core.service.ApiResponseMessageService;
import uz.com.markethub.core.util.HelperUtil;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Log4j2
@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionController {
    private final ApiResponseMessageService responseMessageService;
  //  private final ExceptionHandlerService exceptionHandlerService;

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse<?>> globalException(Exception exception) {

        Long logId = HelperUtil.generateLogId();

        ResponseEntity<ApiResponse<?>> response = ResponseEntity
                .badRequest()
                .body(new ApiResponse<>(
                                responseMessageService.findByApiStatus(logId, ApiStatus.ERR_UNEXPECTED),
                                exception.getMessage()
                        )
                );

        log.error("Response -- data: {} ", response);
        //exceptionHandlerService.sendException(exception, logId);
        return response;
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> resourceNotFound(ResourceNotFoundException exception) {
        ApiResponseMessage apiResponseMessage = responseMessageService.findByApiStatus(exception.getLogId(), exception.getApiStatus());

        ResponseEntity<ApiResponse<Object>> response = ResponseEntity
                .badRequest()
                .body(new ApiResponse<>(apiResponseMessage, exception.getResource()));

        log.error("Response -- logId: {}, data: {} ", exception.getLogId(), response);
        return response;
    }

    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<ApiResponse<?>> invalidParameter(InvalidParameterException exception) {
        ApiResponseMessage apiResponseMessage = responseMessageService.findByApiStatus(exception.getLogId(), exception.getApiStatus());

        ResponseEntity<ApiResponse<?>> response = ResponseEntity
                .badRequest()
                .body(new ApiResponse<>(
                        apiResponseMessage,
                        exception.getResource())
                );

        log.error("Response -- logId: {}, data: {} ", exception.getLogId(), response);
        return response;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<List<Map<String, String>>>> handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
        Long logId = HelperUtil.generateLogId();
        ApiResponseMessage apiResponseMessage = responseMessageService.findByApiStatus(logId, ApiStatus.ERR_DUPLICATE_VALUE);

        List<Map<String, String>> fields = new LinkedList<>();
        String exceptionMessage = exception.getMessage();

        // to get fieldName from exceptionMessage
        int start = exceptionMessage.indexOf("(") + 1;
        int end = exceptionMessage.indexOf(")");

        String fieldName = exceptionMessage.substring(start, end);
        exceptionMessage = "duplicate key value violates unique constraint";

        Map<String, String> map = new LinkedHashMap<>();
        map.put("name", fieldName);
        map.put("message", exceptionMessage);

        fields.add(map);

        ResponseEntity<ApiResponse<List<Map<String, String>>>> response = ResponseEntity
                .badRequest()
                .body(new ApiResponse<>(
                        apiResponseMessage,
                        fields)
                );

        log.error("Response --  data: {} ", response);

        return response;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<List<Map<String, String>>>> validationExceptions(MethodArgumentNotValidException exception) {
        Long logId = HelperUtil.generateLogId();
        ApiResponseMessage apiResponseMessage = responseMessageService.findByApiStatus(logId, ApiStatus.ERR_VALIDATION);

        List<Map<String, String>> fields = new LinkedList<>();

        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String field = ((FieldError) error).getField();
            String message = error.getDefaultMessage();

            Map<String, String> map = new LinkedHashMap<>();
            map.put("name", field);
            map.put("message", message);

            fields.add(map);
        });

        ResponseEntity<ApiResponse<List<Map<String, String>>>> response = ResponseEntity
                .badRequest()
                .body(new ApiResponse<>(
                        apiResponseMessage,
                        fields)
                );

        log.error("Response --  data: {} ", response);
        return response;
    }
}

package uz.com.markethub.core.dto;

import lombok.Getter;
import lombok.ToString;
import uz.com.markethub.core.record.ApiResponseMessage;

@Getter
@ToString
public class ApiResponse<T> {
    private final int code;
    private final String message;
    private T data;

    public ApiResponse(ApiResponseMessage messageDTO) {
        this.code = messageDTO.code();
        this.message = messageDTO.message();
    }

    public ApiResponse(ApiResponseMessage messageDTO, T data) {
        this.code = messageDTO.code();
        this.message = messageDTO.message();
        this.data = data;
    }
}

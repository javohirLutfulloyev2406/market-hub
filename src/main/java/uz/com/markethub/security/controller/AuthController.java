package uz.com.markethub.security.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.com.markethub.core.dto.ApiResponse;
import uz.com.markethub.core.record.ApiStatus;
import uz.com.markethub.core.service.ApiResponseMessageService;
import uz.com.markethub.core.util.HelperUtil;
import uz.com.markethub.security.dto.AccessTokenDTO;
import uz.com.markethub.security.service.AuthService;
import uz.com.markethub.security.vm.LoginVM;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {
    private final AuthService service;
    private final ApiResponseMessageService responseMessageService;

    @PostMapping("/authenticate")
    public ResponseEntity<ApiResponse<AccessTokenDTO>> authenticate(@Valid @RequestBody LoginVM vm,
                                                                    HttpServletRequest request,
                                                                    HttpServletResponse response) {
        Long logId = HelperUtil.generateLogId();
        log.debug("logId: {}, Authorization in the system by username: {}", logId, vm.getUsername());
        var authenticated = service.authenticate(logId, vm, request, response);

        return ResponseEntity
                .ok()
                .body(new ApiResponse<>(
                        responseMessageService.findByApiStatus(logId, ApiStatus.OK),
                        authenticated)
                );
    }
}

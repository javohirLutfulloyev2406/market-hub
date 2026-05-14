package uz.com.markethub.module.cart.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.com.markethub.core.dto.ApiResponse;
import uz.com.markethub.core.record.ApiStatus;
import uz.com.markethub.core.service.ApiResponseMessageService;
import uz.com.markethub.core.util.HelperUtil;
import uz.com.markethub.module.User.util.HelperRequestUtil;
import uz.com.markethub.module.cart.dto.CartDTO;
import uz.com.markethub.module.cart.service.CartService;
import uz.com.markethub.security.util.SecurityUtils;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService service;
    private final ApiResponseMessageService responseMessageService;

    @GetMapping
    @PreAuthorize("hasAuthority('CART_READ')")
    public ResponseEntity<ApiResponse<CartDTO.Full>> getMyCart() {
        Long logId = HelperUtil.generateLogId();
        log.debug("Request -- logId: {}, userId: {}, apiPartnerInfo: {}, getMyCart",
                logId,
                SecurityUtils.getAuthenticatedUserId(),
                HelperRequestUtil.getApiPartnerInfo());

        CartDTO.Full result = service.getMyCart(logId);

        ResponseEntity<ApiResponse<CartDTO.Full>> response = ResponseEntity
                .ok()
                .body(new ApiResponse<>(
                        responseMessageService.findByApiStatus(logId, ApiStatus.OK),
                        result));

        log.debug("Response -- logId: {}, data: {}", logId, response);
        return response;
    }

    @PostMapping("/items")
    @PreAuthorize("hasAuthority('CART_MANAGE')")
    public ResponseEntity<ApiResponse<CartDTO.Full>> addItem(@Valid @RequestBody CartDTO.AddOrUpdate dto) {
        Long logId = HelperUtil.generateLogId();
        log.debug("Request -- logId: {}, userId: {}, apiPartnerInfo: {}, addItem data: {}",
                logId,
                SecurityUtils.getAuthenticatedUserId(),
                HelperRequestUtil.getApiPartnerInfo(),
                dto);

        CartDTO.Full result = service.addItem(logId, dto);

        ResponseEntity<ApiResponse<CartDTO.Full>> response = ResponseEntity
                .ok()
                .body(new ApiResponse<>(
                        responseMessageService.findByApiStatus(logId, ApiStatus.UPDATED),
                        result));

        log.debug("Response -- logId: {}, data: {}", logId, response);
        return response;
    }

    @PutMapping("/items/{productId}")
    @PreAuthorize("hasAuthority('CART_MANAGE')")
    public ResponseEntity<ApiResponse<CartDTO.Full>> updateItemQuantity(
            @PathVariable Long productId,
            @Valid @RequestBody CartDTO.AddOrUpdate dto) {
        Long logId = HelperUtil.generateLogId();
        log.debug("Request -- logId: {}, userId: {}, apiPartnerInfo: {}, updateItem productId: {}, data: {}",
                logId,
                SecurityUtils.getAuthenticatedUserId(),
                HelperRequestUtil.getApiPartnerInfo(),
                productId,
                dto);

        CartDTO.Full result = service.updateItemQuantity(logId, productId, dto);

        ResponseEntity<ApiResponse<CartDTO.Full>> response = ResponseEntity
                .ok()
                .body(new ApiResponse<>(
                        responseMessageService.findByApiStatus(logId, ApiStatus.UPDATED),
                        result));

        log.debug("Response -- logId: {}, data: {}", logId, response);
        return response;
    }

    @DeleteMapping("/items/{productId}")
    @PreAuthorize("hasAuthority('CART_MANAGE')")
    public ResponseEntity<ApiResponse<CartDTO.Full>> removeItem(@PathVariable Long productId) {
        Long logId = HelperUtil.generateLogId();
        log.debug("Request -- logId: {}, userId: {}, apiPartnerInfo: {}, removeItem productId: {}",
                logId,
                SecurityUtils.getAuthenticatedUserId(),
                HelperRequestUtil.getApiPartnerInfo(),
                productId);

        CartDTO.Full result = service.removeItem(logId, productId);

        ResponseEntity<ApiResponse<CartDTO.Full>> response = ResponseEntity
                .ok()
                .body(new ApiResponse<>(
                        responseMessageService.findByApiStatus(logId, ApiStatus.UPDATED),
                        result));

        log.debug("Response -- logId: {}, data: {}", logId, response);
        return response;
    }
}
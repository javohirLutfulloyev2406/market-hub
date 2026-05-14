package uz.com.markethub.module.order.controller;

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
import uz.com.markethub.module.order.dto.CheckoutDTO;
import uz.com.markethub.module.order.dto.OrderDTO;
import uz.com.markethub.module.order.service.OrderService;
import uz.com.markethub.security.util.SecurityUtils;

import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class BuyerOrderController {

    private final OrderService service;
    private final ApiResponseMessageService responseMessageService;

    @PostMapping("/checkout")
    @PreAuthorize("hasAuthority('ORDER_CHECKOUT')")
    public ResponseEntity<ApiResponse<List<OrderDTO.Full>>> checkout(@Valid @RequestBody CheckoutDTO dto) {
        Long logId = HelperUtil.generateLogId();
        log.debug("Request -- logId: {}, userId: {}, apiPartnerInfo: {}, checkout data: {}",
                logId,
                SecurityUtils.getAuthenticatedUserId(),
                HelperRequestUtil.getApiPartnerInfo(),
                dto);

        List<OrderDTO.Full> result = service.checkout(logId, dto);

        ResponseEntity<ApiResponse<List<OrderDTO.Full>>> response = ResponseEntity
                .ok()
                .body(new ApiResponse<>(
                        responseMessageService.findByApiStatus(logId, ApiStatus.CREATED),
                        result));

        log.debug("Response -- logId: {}, data: {}", logId, response);
        return response;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ORDER_READ')")
    public ResponseEntity<ApiResponse<List<OrderDTO.Full>>> findAllMyOrders() {
        Long logId = HelperUtil.generateLogId();
        log.debug("Request -- logId: {}, userId: {}, apiPartnerInfo: {}, findAllMyOrders",
                logId,
                SecurityUtils.getAuthenticatedUserId(),
                HelperRequestUtil.getApiPartnerInfo());

        List<OrderDTO.Full> result = service.findAllMyOrders(logId);

        ResponseEntity<ApiResponse<List<OrderDTO.Full>>> response = ResponseEntity
                .ok()
                .body(new ApiResponse<>(
                        responseMessageService.findByApiStatus(logId, ApiStatus.OK),
                        result));

        log.debug("Response -- logId: {}, data: {}", logId, response);
        return response;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ORDER_READ')")
    public ResponseEntity<ApiResponse<OrderDTO.Full>> findMyOrderById(@PathVariable Long id) {
        Long logId = HelperUtil.generateLogId();
        log.debug("Request -- logId: {}, userId: {}, apiPartnerInfo: {}, findMyOrderById: {}",
                logId,
                SecurityUtils.getAuthenticatedUserId(),
                HelperRequestUtil.getApiPartnerInfo(),
                id);

        OrderDTO.Full result = service.findMyOrderById(logId, id);

        ResponseEntity<ApiResponse<OrderDTO.Full>> response = ResponseEntity
                .ok()
                .body(new ApiResponse<>(
                        responseMessageService.findByApiStatus(logId, ApiStatus.OK),
                        result));

        log.debug("Response -- logId: {}, data: {}", logId, response);
        return response;
    }
}
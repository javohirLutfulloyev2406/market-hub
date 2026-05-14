package uz.com.markethub.module.order.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import uz.com.markethub.core.dto.ApiResponse;
import uz.com.markethub.core.record.ApiStatus;
import uz.com.markethub.core.service.ApiResponseMessageService;
import uz.com.markethub.core.util.HelperUtil;
import uz.com.markethub.core.util.PaginationUtil;
import uz.com.markethub.module.User.util.HelperRequestUtil;
import uz.com.markethub.module.order.dto.OrderDTO;
import uz.com.markethub.module.order.service.OrderService;
import uz.com.markethub.security.util.SecurityUtils;

import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/seller/orders")
public class SellerOrderController {

    private final OrderService service;
    private final ApiResponseMessageService responseMessageService;

    @GetMapping("/paged")
    @PreAuthorize("hasAuthority('SELLER_ORDER_READ')")
    public ResponseEntity<ApiResponse<List<OrderDTO>>> findAllSellerOrders(Pageable pageable) {
        Long logId = HelperUtil.generateLogId();
        log.debug("Request -- logId: {}, userId: {}, apiPartnerInfo: {}, findAllSellerOrders pageable: {}",
                logId,
                SecurityUtils.getAuthenticatedUserId(),
                HelperRequestUtil.getApiPartnerInfo(),
                pageable);

        Page<OrderDTO> result = service.findAllSellerOrders(logId, pageable);

        ResponseEntity<ApiResponse<List<OrderDTO>>> response = ResponseEntity
                .ok()
                .headers(PaginationUtil.generatePaginationHttpHeaders(
                        ServletUriComponentsBuilder.fromCurrentRequest(), result))
                .body(new ApiResponse<>(
                        responseMessageService.findByApiStatus(logId, ApiStatus.OK),
                        result.getContent()));

        log.debug("Response -- logId: {}, data: {}", logId, response);
        return response;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SELLER_ORDER_READ')")
    public ResponseEntity<ApiResponse<OrderDTO.Full>> findSellerOrderById(@PathVariable Long id) {
        Long logId = HelperUtil.generateLogId();
        log.debug("Request -- logId: {}, userId: {}, apiPartnerInfo: {}, findSellerOrderById: {}",
                logId,
                SecurityUtils.getAuthenticatedUserId(),
                HelperRequestUtil.getApiPartnerInfo(),
                id);

        OrderDTO.Full result = service.findSellerOrderById(logId, id);

        ResponseEntity<ApiResponse<OrderDTO.Full>> response = ResponseEntity
                .ok()
                .body(new ApiResponse<>(
                        responseMessageService.findByApiStatus(logId, ApiStatus.OK),
                        result));

        log.debug("Response -- logId: {}, data: {}", logId, response);
        return response;
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAuthority('SELLER_ORDER_UPDATE')")
    public ResponseEntity<ApiResponse<OrderDTO.Full>> updateOrderStatus(
            @PathVariable Long id,
            @Valid @RequestBody OrderDTO.UpdateStatus dto) {
        Long logId = HelperUtil.generateLogId();
        log.debug("Request -- logId: {}, userId: {}, apiPartnerInfo: {}, updateOrderStatus orderId: {}, data: {}",
                logId,
                SecurityUtils.getAuthenticatedUserId(),
                HelperRequestUtil.getApiPartnerInfo(),
                id,
                dto);

        OrderDTO.Full result = service.updateOrderStatus(logId, id, dto);

        ResponseEntity<ApiResponse<OrderDTO.Full>> response = ResponseEntity
                .ok()
                .body(new ApiResponse<>(
                        responseMessageService.findByApiStatus(logId, ApiStatus.UPDATED),
                        result));

        log.debug("Response -- logId: {}, data: {}", logId, response);
        return response;
    }
}
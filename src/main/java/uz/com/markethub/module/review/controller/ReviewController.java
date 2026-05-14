package uz.com.markethub.module.review.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
import uz.com.markethub.module.review.dto.ReviewDTO;
import uz.com.markethub.module.review.service.ReviewService;
import uz.com.markethub.security.util.SecurityUtils;

import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewService service;
    private final ApiResponseMessageService responseMessageService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<ReviewDTO.Full>> create(@Valid @RequestBody ReviewDTO.CreateRequest request) {
        Long logId = HelperUtil.generateLogId();
        log.debug("Request -- logId: {}, userId: {}, apiPartnerInfo: {}, create review: {}",
                logId,
                SecurityUtils.getAuthenticatedUserId(),
                HelperRequestUtil.getApiPartnerInfo(),
                request);

        ReviewDTO.Full result = service.create(logId, request);

        ResponseEntity<ApiResponse<ReviewDTO.Full>> response = ResponseEntity
                .ok()
                .body(new ApiResponse<>(
                        responseMessageService.findByApiStatus(logId, ApiStatus.CREATED),
                        result));

        log.debug("Response -- logId: {}, data: {}", logId, response);
        return response;
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<List<ReviewDTO.Full>>> findAllByProduct(
            @PathVariable Long productId,
            @PageableDefault(size = 10) Pageable pageable) {
        Long logId = HelperUtil.generateLogId();
        log.debug("Request -- logId: {}, findAllByProductId: {}, pageable: {}",
                logId, productId, pageable);

        Page<ReviewDTO.Full> result = service.findAllByProductId(logId, productId, pageable);

        ResponseEntity<ApiResponse<List<ReviewDTO.Full>>> response = ResponseEntity
                .ok()
                .headers(PaginationUtil.generatePaginationHttpHeaders(
                        ServletUriComponentsBuilder.fromCurrentRequest(), result))
                .body(new ApiResponse<>(
                        responseMessageService.findByApiStatus(logId, ApiStatus.OK),
                        result.getContent()));

        log.debug("Response -- logId: {}, data: {}", logId, response);
        return response;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        Long logId = HelperUtil.generateLogId();
        log.debug("Request -- logId: {}, userId: {}, apiPartnerInfo: {}, deleteBy id: {}",
                logId,
                SecurityUtils.getAuthenticatedUserId(),
                HelperRequestUtil.getApiPartnerInfo(),
                id);

        service.deleteById(logId, id);

        ResponseEntity<ApiResponse<Object>> response = ResponseEntity
                .ok()
                .body(new ApiResponse<>(responseMessageService.findByApiStatus(logId, ApiStatus.DELETED)));

        log.debug("Response -- logId: {}, data: {}", logId, response);
        return response;
    }
}
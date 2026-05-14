package uz.com.markethub.module.product.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import uz.com.markethub.core.dto.ApiResponse;
import uz.com.markethub.core.exception.InvalidParameterException;
import uz.com.markethub.core.exception.ResourceNotFoundException;
import uz.com.markethub.core.record.ApiStatus;
import uz.com.markethub.core.service.ApiResponseMessageService;
import uz.com.markethub.core.util.HelperUtil;
import uz.com.markethub.core.util.PaginationUtil;
import uz.com.markethub.module.User.util.HelperRequestUtil;
import uz.com.markethub.module.product.dto.ProductDTO;
import uz.com.markethub.module.product.service.ProductService;
import uz.com.markethub.security.util.SecurityUtils;

import java.util.List;
import java.util.Objects;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService service;
    private final ApiResponseMessageService responseMessageService;

    @PostMapping
    @PreAuthorize("hasAuthority('PRODUCT_CREATE')")
    public ResponseEntity<ApiResponse<ProductDTO.Full>> create(@Valid @RequestBody ProductDTO.CreateOrUpdate dto) {
        Long logId = HelperUtil.generateLogId();
        log.debug("Request -- logId: {}, userId: {}, apiPartnerInfo: {}, create data: {}",
                logId,
                SecurityUtils.getAuthenticatedUserId(),
                HelperRequestUtil.getApiPartnerInfo(),
                dto);

        if (Objects.nonNull(dto.getId())) {
            throw new InvalidParameterException(logId, dto.getId(), ApiStatus.ERR_ID_IS_NOT_NULL);
        }

        var result = service.create(logId, dto);

        ResponseEntity<ApiResponse<ProductDTO.Full>> response = ResponseEntity
                .ok()
                .body(new ApiResponse<>(
                        responseMessageService.findByApiStatus(logId, ApiStatus.CREATED),
                        result));

        log.debug("Response -- logId: {}, data: {}", logId, response);
        return response;
    }

    @GetMapping("/paged")
    @PreAuthorize("hasAuthority('PRODUCT_READ_ALL')")
    public ResponseEntity<ApiResponse<List<ProductDTO.Full>>> findAllPaged(
            @RequestParam(required = false) MultiValueMap<String, String> filters,
            Pageable pageable) {
        Long logId = HelperUtil.generateLogId();
        log.debug("Request -- logId: {}, userId: {}, apiPartnerInfo: {}, findAllBy pageable: {}",
                logId,
                SecurityUtils.getAuthenticatedUserId(),
                HelperRequestUtil.getApiPartnerInfo(),
                pageable);

        Page<ProductDTO.Full> result = service.findAllPaged(logId, filters, pageable);

        ResponseEntity<ApiResponse<List<ProductDTO.Full>>> response = ResponseEntity
                .ok()
                .headers(PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), result))
                .body(new ApiResponse<>(
                        responseMessageService.findByApiStatus(logId, ApiStatus.OK),
                        result.getContent()));

        log.debug("Response -- logId: {}, data: {}", logId, response);
        return response;
    }

    @GetMapping("/short-info")
    @PreAuthorize("hasAuthority('PRODUCT_READ_SHORT')")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> findAllShortInfo() {
        Long logId = HelperUtil.generateLogId();
        log.debug("Request -- logId: {}, userId: {}, apiPartnerInfo: {}, findAllShortInfo",
                logId,
                SecurityUtils.getAuthenticatedUserId(),
                HelperRequestUtil.getApiPartnerInfo());

        List<ProductDTO> result = service.findAllShortInfo(logId);

        ResponseEntity<ApiResponse<List<ProductDTO>>> response = ResponseEntity
                .ok()
                .body(new ApiResponse<>(
                        responseMessageService.findByApiStatus(logId, ApiStatus.OK),
                        result));

        log.debug("Response -- logId: {}, data: {}", logId, response);
        return response;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PRODUCT_READ_DETAILS')")
    public ResponseEntity<ApiResponse<ProductDTO.Full>> findById(@PathVariable Long id) {
        Long logId = HelperUtil.generateLogId();
        log.debug("Request -- logId: {}, userId: {}, apiPartnerInfo: {}, findBy id: {}",
                logId,
                SecurityUtils.getAuthenticatedUserId(),
                HelperRequestUtil.getApiPartnerInfo(),
                id);

        var result = service.findById(logId, id);

        ResponseEntity<ApiResponse<ProductDTO.Full>> response = ResponseEntity
                .ok()
                .body(new ApiResponse<>(
                        responseMessageService.findByApiStatus(logId, ApiStatus.OK),
                        result));

        log.debug("Response -- logId: {}, data: {}", logId, response);
        return response;
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('PRODUCT_UPDATE')")
    public ResponseEntity<ApiResponse<ProductDTO.Full>> update(
            @PathVariable Long id,
            @Valid @RequestBody ProductDTO.CreateOrUpdate dto) {
        Long logId = HelperUtil.generateLogId();
        log.debug("Request -- logId: {}, userId: {}, apiPartnerInfo: {}, update data: {}",
                logId,
                SecurityUtils.getAuthenticatedUserId(),
                HelperRequestUtil.getApiPartnerInfo(),
                dto);

        var result = service.update(logId, id, dto);

        ResponseEntity<ApiResponse<ProductDTO.Full>> response = ResponseEntity
                .ok()
                .body(new ApiResponse<>(
                        responseMessageService.findByApiStatus(logId, ApiStatus.UPDATED),
                        result));

        log.debug("Response -- logId: {}, data: {}", logId, response);
        return response;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('PRODUCT_DELETE')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Long logId = HelperUtil.generateLogId();
        log.debug("Request -- logId: {}, userId: {}, apiPartnerInfo: {}, deleteBy id: {}",
                logId,
                SecurityUtils.getAuthenticatedUserId(),
                HelperRequestUtil.getApiPartnerInfo(),
                id);

        if (!service.existsById(logId, id)) {
            throw new ResourceNotFoundException(logId, id, ApiStatus.ERR_ID_NOT_FOUND);
        }

        service.deleteById(logId, id);

        ResponseEntity<ApiResponse<Object>> response = ResponseEntity
                .ok()
                .body(new ApiResponse<>(responseMessageService.findByApiStatus(logId, ApiStatus.DELETED)));

        log.debug("Response -- logId: {}, data: {}", logId, response);
        return response;
    }
}
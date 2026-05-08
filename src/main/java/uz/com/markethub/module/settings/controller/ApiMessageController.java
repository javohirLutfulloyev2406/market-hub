package uz.com.markethub.module.settings.controller;

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
import uz.com.markethub.core.exception.ResourceNotFoundException;
import uz.com.markethub.core.record.ApiStatus;
import uz.com.markethub.core.service.ApiResponseMessageService;
import uz.com.markethub.core.util.HelperUtil;
import uz.com.markethub.core.util.PaginationUtil;
import uz.com.markethub.module.User.util.HelperRequestUtil;
import uz.com.markethub.module.settings.dto.ApiMessageDTO;
import uz.com.markethub.module.settings.service.cache.ApiMessageServiceCache;
import uz.com.markethub.module.settings.service.facade.ApiMessageServiceFacade;
import uz.com.markethub.security.util.SecurityUtils;

import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/api-messages")
public class ApiMessageController {
    private final ApiMessageServiceCache serviceCache;
    private final ApiMessageServiceFacade serviceFacade;
    private final ApiResponseMessageService responseMessageService;

    @PostMapping
    @PreAuthorize("hasAuthority('API_MESSAGE_CREATE')")
    public ResponseEntity<ApiResponse<ApiMessageDTO>> create(@Valid @RequestBody ApiMessageDTO dto) {
        Long logId = HelperUtil.generateLogId();
        log.debug("Request -- logId: {}, userId: {}, apiPartnerInfo: {}, create data: {}",
                logId,
                SecurityUtils.getAuthenticatedUserId(),
                HelperRequestUtil.getApiPartnerInfo(),
                dto);

        var result = serviceFacade.createEntityAndCache(logId, dto);

        ResponseEntity<ApiResponse<ApiMessageDTO>> response = ResponseEntity
                .ok()
                .body(new ApiResponse<>(
                        responseMessageService.findByApiStatus(logId, ApiStatus.CREATED),
                        result));

        log.debug("Response -- logId: {}, data: {}", logId, response);
        return response;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('API_MESSAGE_READ_ALL')")
    public ResponseEntity<ApiResponse<List<ApiMessageDTO.Full>>> findAll() {
        Long logId = HelperUtil.generateLogId();
        log.debug("Request -- logId: {}, userId: {}, apiPartnerInfo: {}, findAll",
                logId,
                SecurityUtils.getAuthenticatedUserId(),
                HelperRequestUtil.getApiPartnerInfo());

        var result = serviceCache.findAll(logId);

        ResponseEntity<ApiResponse<List<ApiMessageDTO.Full>>> response = ResponseEntity
                .ok()
                .body(new ApiResponse<>(
                        responseMessageService.findByApiStatus(logId, ApiStatus.OK),
                        result)
                );

        log.debug("Response -- logId: {}, data: {}", logId, response);
        return response;
    }

    @GetMapping("/paged")
    @PreAuthorize("hasAuthority('API_MESSAGE_READ_ALL_PAGED')")
    public ResponseEntity<ApiResponse<List<ApiMessageDTO.Full>>> findAllPaged(@RequestParam(required = false) MultiValueMap<String, String> filters, Pageable pageable) {
        Long logId = HelperUtil.generateLogId();
        log.debug("Request -- logId: {}, userId: {}, apiPartnerInfo: {}, findAllBy pageable: {}",
                logId,
                SecurityUtils.getAuthenticatedUserId(),
                HelperRequestUtil.getApiPartnerInfo(),
                pageable);

        Page<ApiMessageDTO.Full> result = serviceCache.findAllPaged(logId, filters, pageable);

        ResponseEntity<ApiResponse<List<ApiMessageDTO.Full>>> response = ResponseEntity
                .ok()
                .headers(PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), result))
                .body(new ApiResponse<>(
                        responseMessageService.findByApiStatus(logId, ApiStatus.OK),
                        result.getContent())
                );

        log.debug("Response -- logId: {}, data: {}", logId, response);
        return response;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('API_MESSAGE_READ_DETAILS')")
    public ResponseEntity<ApiResponse<ApiMessageDTO.Full>> findById(@PathVariable Long id) {
        Long logId = HelperUtil.generateLogId();
        log.debug("Request -- logId: {}, userId: {}, apiPartnerInfo: {}, findBy id: {}",
                logId,
                SecurityUtils.getAuthenticatedUserId(),
                HelperRequestUtil.getApiPartnerInfo(),
                id);

        var result = serviceCache.findById(logId, id);

        ResponseEntity<ApiResponse<ApiMessageDTO.Full>> response = ResponseEntity
                .ok()
                .body(new ApiResponse<>(responseMessageService.findByApiStatus(logId, ApiStatus.OK)
                        , result)
                );

        log.debug("Response -- logId: {}, data: {}", logId, response);
        return response;
    }

    @GetMapping("/{key}/key")
    @PreAuthorize("hasAuthority('API_MESSAGE_READ_BY_KEY')")
    public ResponseEntity<ApiResponse<ApiMessageDTO.Full>> findByKey(@PathVariable String key) {
        Long logId = HelperUtil.generateLogId();
        log.debug("Request -- logId: {}, userId: {}, apiPartnerInfo: {}, findBy name: {}",
                logId,
                SecurityUtils.getAuthenticatedUserId(),
                HelperRequestUtil.getApiPartnerInfo(),
                key);

        var result = serviceCache.findByKey(logId, key);

        ResponseEntity<ApiResponse<ApiMessageDTO.Full>> response = ResponseEntity
                .ok()
                .body(new ApiResponse
                        <>(responseMessageService.findByApiStatus(logId, ApiStatus.OK), result));

        log.debug("Response -- logId: {}, data: {}", logId, response);
        return response;
    }

    @GetMapping("/reload-cache")
    @PreAuthorize("hasAuthority('API_MESSAGE_RELOAD_CACHE')")
    public ResponseEntity<?> reloadCache() {
        Long logId = HelperUtil.generateLogId();
        log.debug("Request -- logId: {}, userId: {}, apiPartnerInfo: {}, refresh Cache",
                logId,
                SecurityUtils.getAuthenticatedUserId(),
                HelperRequestUtil.getApiPartnerInfo());

        serviceCache.initCache();

        ResponseEntity<?> response = ResponseEntity
                .ok()
                .body(new ApiResponse
                        <>(responseMessageService.findByApiStatus(logId, ApiStatus.OK)));

        log.debug("Response -- logId: {}, data: {}", logId, response);
        return response;
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('API_MESSAGE_UPDATE')")
    public ResponseEntity<ApiResponse<ApiMessageDTO.Full>> update(@PathVariable Long id, @Valid @RequestBody ApiMessageDTO.Full dto) {
        Long logId = HelperUtil.generateLogId();
        log.debug("Request -- logId: {}, userId: {}, apiPartnerInfo: {}, update data: {}",
                logId,
                SecurityUtils.getAuthenticatedUserId(),
                HelperRequestUtil.getApiPartnerInfo(),
                dto);

        var result = serviceFacade.updateEntityAndCache(logId, id, dto);

        ResponseEntity<ApiResponse<ApiMessageDTO.Full>> response = ResponseEntity
                .ok()
                .body(new ApiResponse
                        <>(responseMessageService.findByApiStatus(logId, ApiStatus.UPDATED),
                        result)
                );

        log.debug("Response -- logId: {}, data: {}", logId, response);
        return response;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('API_MESSAGE_DELETE')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Long logId = HelperUtil.generateLogId();
        log.debug("Request -- logId: {}, userId: {}, apiPartnerInfo: {}, deleteBy id: {}",
                logId,
                SecurityUtils.getAuthenticatedUserId(),
                HelperRequestUtil.getApiPartnerInfo(),
                id);

        if (!serviceCache.existsById(logId, id)) {
            throw new ResourceNotFoundException(logId, id, ApiStatus.ERR_ID_NOT_FOUND);
        }

        serviceFacade.deleteEntityAndCacheId(logId, id);

        ResponseEntity<ApiResponse<Object>> response = ResponseEntity
                .ok()
                .body(new ApiResponse<>(responseMessageService.findByApiStatus(logId, ApiStatus.DELETED))
                );

        log.debug("Response -- logId: {}, data: {}", logId, response);
        return response;
    }
}

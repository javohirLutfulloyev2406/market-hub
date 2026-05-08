package uz.com.markethub.module.export.controller;

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
import uz.com.markethub.core.exception.ResourceNotFoundException;
import uz.com.markethub.core.record.ApiStatus;
import uz.com.markethub.core.service.ApiResponseMessageService;
import uz.com.markethub.core.util.HelperUtil;
import uz.com.markethub.core.util.PaginationUtil;
import uz.com.markethub.module.User.util.HelperRequestUtil;
import uz.com.markethub.module.export.dto.ExportDTO;
import uz.com.markethub.module.export.service.ExportService;
import uz.com.markethub.module.export.service.ExportServiceFacade;
import uz.com.markethub.security.util.SecurityUtils;


import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/export")
public class ExportController {

    private final ExportService exportService;
    private final ExportServiceFacade exportServiceFacade;
    private final ApiResponseMessageService responseMessageService;

    @PostMapping
    @PreAuthorize("hasAuthority('EXPORT_CREATE')")
    public ResponseEntity<ApiResponse<ExportDTO.Full>> create(@Valid @RequestBody ExportDTO dto) {
        Long logId = HelperUtil.generateLogId();
        log.debug("Request -- logId: {}, userId: {}, apiPartnerInfo: {}, create data: {}",
                logId,
                SecurityUtils.getAuthenticatedUserId(),
                HelperRequestUtil.getApiPartnerInfo(),
                dto);

        var result = exportServiceFacade.createAndExportData(logId, dto, null);

        ResponseEntity<ApiResponse<ExportDTO.Full>> response = ResponseEntity
                .ok()
                .body(new ApiResponse<>(
                        responseMessageService.findByApiStatus(logId, ApiStatus.CREATED),
                        result));

        log.debug("Response -- logId: {}, data: {}", logId, response);
        return response;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('EXPORT_READ_ALL')")
    public ResponseEntity<ApiResponse<List<ExportDTO.Full>>> findAll() {
        Long logId = HelperUtil.generateLogId();
        log.debug("Request -- logId: {}, userId: {}, apiPartnerInfo: {}, findAll",
                logId,
                SecurityUtils.getAuthenticatedUserId(),
                HelperRequestUtil.getApiPartnerInfo());

        var result = exportService.findAll(logId);

        ResponseEntity<ApiResponse<List<ExportDTO.Full>>> response = ResponseEntity
                .ok()
                .body(new ApiResponse<>(
                        responseMessageService.findByApiStatus(logId, ApiStatus.OK),
                        result)
                );

        log.debug("Response -- logId: {}, data: {}", logId, response);
        return response;
    }

    @GetMapping("/paged")
    @PreAuthorize("hasAuthority('EXPORT_READ_ALL_PAGED')")
    public ResponseEntity<ApiResponse<List<ExportDTO.Full>>> findAllPaged(Pageable pageable) {
        Long logId = HelperUtil.generateLogId();
        log.debug("Request -- logId: {}, userId: {}, apiPartnerInfo: {}, findAllBy pageable: {}",
                logId,
                SecurityUtils.getAuthenticatedUserId(),
                HelperRequestUtil.getApiPartnerInfo(),
                pageable);

        Page<ExportDTO.Full> result = exportService.findAllPaged(logId, pageable);

        ResponseEntity<ApiResponse<List<ExportDTO.Full>>> response = ResponseEntity
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
    @PreAuthorize("hasAuthority('EXPORT_READ_DETAILS')")
    public ResponseEntity<ApiResponse<ExportDTO.Full>> findById(@PathVariable Long id) {
        Long logId = HelperUtil.generateLogId();
        log.debug("Request -- logId: {}, userId: {}, apiPartnerInfo: {}, findBy id: {}",
                logId,
                SecurityUtils.getAuthenticatedUserId(),
                HelperRequestUtil.getApiPartnerInfo(),
                id);

        var result = exportService.findById(logId, id);

        ResponseEntity<ApiResponse<ExportDTO.Full>> response = ResponseEntity
                .ok()
                .body(new ApiResponse<>(responseMessageService.findByApiStatus(logId, ApiStatus.OK)
                        , result)
                );

        log.debug("Response -- logId: {}, data: {}", logId, response);
        return response;
    }



    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('EXPORT_UPDATE')")
    public ResponseEntity<ApiResponse<ExportDTO.Full>> update(@PathVariable Long id, @Valid @RequestBody ExportDTO dto) {
        Long logId = HelperUtil.generateLogId();
        log.debug("Request -- logId: {}, userId: {}, apiPartnerInfo: {}, update data: {}",
                logId,
                SecurityUtils.getAuthenticatedUserId(),
                HelperRequestUtil.getApiPartnerInfo(),
                dto);

        var result = exportService.update(logId, id, dto);

        ResponseEntity<ApiResponse<ExportDTO.Full>> response = ResponseEntity
                .ok()
                .body(new ApiResponse
                        <>(responseMessageService.findByApiStatus(logId, ApiStatus.UPDATED),
                        result)
                );

        log.debug("Response -- logId: {}, data: {}", logId, response);
        return response;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('EXPORT_DELETE')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Long logId = HelperUtil.generateLogId();
        log.debug("Request -- logId: {}, userId: {}, apiPartnerInfo: {}, deleteBy id: {}",
                logId,
                SecurityUtils.getAuthenticatedUserId(),
                HelperRequestUtil.getApiPartnerInfo(),
                id);

        if (!exportService.existsById(logId, id)) {
            throw new ResourceNotFoundException(logId, id, ApiStatus.ERR_ID_NOT_FOUND);
        }

        exportService.deleteById(logId, id);

        ResponseEntity<ApiResponse<Object>> response = ResponseEntity
                .ok()
                .body(new ApiResponse<>(responseMessageService.findByApiStatus(logId, ApiStatus.DELETED))
                );

        log.debug("Response -- logId: {}, data: {}", logId, response);
        return response;
    }
}

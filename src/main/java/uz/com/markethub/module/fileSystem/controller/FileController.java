package uz.com.markethub.module.fileSystem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import uz.com.markethub.core.dto.ApiResponse;
import uz.com.markethub.core.exception.ResourceNotFoundException;
import uz.com.markethub.core.record.ApiStatus;
import uz.com.markethub.core.service.ApiResponseMessageService;
import uz.com.markethub.core.util.HelperUtil;
import uz.com.markethub.core.util.PaginationUtil;
import uz.com.markethub.module.User.util.HelperRequestUtil;
import uz.com.markethub.module.fileSystem.dto.FileDTO;
import uz.com.markethub.module.fileSystem.service.FileService;
import uz.com.markethub.security.util.SecurityUtils;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/files")
public class FileController {

    private final FileService fileService;
    private final ApiResponseMessageService responseMessageService;

    @PostMapping("/upload")
    @PreAuthorize("hasAuthority('FILE_UPLOAD')")
    public ResponseEntity<ApiResponse<List<FileDTO.Full>>> upload(@RequestParam("files") List<MultipartFile> files) {
        Long logId = HelperUtil.generateLogId();
        log.debug("Request -- logId: {}, userId: {}, apiPartnerInfo: {}, upload files count: {}",
                logId,
                SecurityUtils.getAuthenticatedUserId(),
                HelperRequestUtil.getApiPartnerInfo(),
                files.size());

        List<FileDTO.Full> result = fileService.uploadFiles(logId, files);

        ResponseEntity<ApiResponse<List<FileDTO.Full>>> response = ResponseEntity
                .ok()
                .body(new ApiResponse<>(responseMessageService.findByApiStatus(logId, ApiStatus.CREATED), result));

        log.debug("Response -- logId: {}, data size: {}", logId, result.size());
        return response;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('FILE_READ_ALL')")
    public ResponseEntity<ApiResponse<List<FileDTO>>> findAll() {
        Long logId = HelperUtil.generateLogId();
        log.debug("Request -- logId: {}, userId: {}, apiPartnerInfo: {}, findAll files",
                logId,
                SecurityUtils.getAuthenticatedUserId(),
                HelperRequestUtil.getApiPartnerInfo());

        var result = fileService.findAll(logId);

        ResponseEntity<ApiResponse<List<FileDTO>>> response = ResponseEntity
                .ok()
                .body(new ApiResponse<>(responseMessageService.findByApiStatus(logId, ApiStatus.OK), result));

        log.debug("Response -- logId: {}, data: {}", logId, response);
        return response;
    }

    @GetMapping("/paged")
    @PreAuthorize("hasAuthority('FILE_READ_ALL_PAGED')")
    public ResponseEntity<ApiResponse<List<FileDTO.Full>>> findAllPaged(@RequestParam(required = false) MultiValueMap<String, String> filters, Pageable pageable) {
        Long logId = HelperUtil.generateLogId();
        log.debug("Request -- logId: {}, userId: {}, apiPartnerInfo: {}, findAllBy pageable: {}",
                logId,
                SecurityUtils.getAuthenticatedUserId(),
                HelperRequestUtil.getApiPartnerInfo(),
                pageable);

        Page<FileDTO.Full> result = fileService.findAllPaged(logId, filters, pageable);

        ResponseEntity<ApiResponse<List<FileDTO.Full>>> response = ResponseEntity
                .ok()
                .headers(PaginationUtil.
                        generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), result))
                .body(new ApiResponse<>(
                        responseMessageService.findByApiStatus(logId, ApiStatus.OK),
                        result.getContent())
                );

        log.debug("Response -- logId: {}, data: {}", logId, response);
        return response;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('FILE_READ_DETAILS')")
    public ResponseEntity<ApiResponse<FileDTO.Full>> findById(@PathVariable Long id) {
        Long logId = HelperUtil.generateLogId();
        log.debug("Request -- logId: {}, userId: {}, apiPartnerInfo: {}, findById file: {}",
                logId,
                SecurityUtils.getAuthenticatedUserId(),
                HelperRequestUtil.getApiPartnerInfo(),
                id);

        var result = fileService.findById(logId, id);

        ResponseEntity<ApiResponse<FileDTO.Full>> response = ResponseEntity
                .ok()
                .body(new ApiResponse<>(responseMessageService.findByApiStatus(logId, ApiStatus.OK), result));

        log.debug("Response -- logId: {}, data: {}", logId, response);
        return response;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('FILE_DELETE')")
    public ResponseEntity<ApiResponse<Object>> delete(@PathVariable Long id) {
        Long logId = HelperUtil.generateLogId();
        log.debug("Request -- logId: {}, userId: {}, apiPartnerInfo: {}, delete file id: {}",
                logId,
                SecurityUtils.getAuthenticatedUserId(),
                HelperRequestUtil.getApiPartnerInfo(),
                id);

        if (!fileService.existsById(logId, id)) {
            throw new ResourceNotFoundException(logId, id, ApiStatus.ERR_ID_NOT_FOUND);
        }

        fileService.deleteById(logId, id);

        ResponseEntity<ApiResponse<Object>> response = ResponseEntity
                .ok()
                .body(new ApiResponse<>(responseMessageService.findByApiStatus(logId, ApiStatus.DELETED)));

        log.debug("Response -- logId: {}, data: {}", logId, response);
        return response;
    }

    @PostMapping("/find-by-ids")
    @PreAuthorize("hasAuthority('FILE_READ_BY_IDS')")
    public ResponseEntity<ApiResponse<List<FileDTO>>> findByIds(@RequestBody List<Long> ids) {
        Long logId = HelperUtil.generateLogId();
        log.debug("Request -- logId: {}, userId: {}, apiPartnerInfo: {}, findByIds: {}",
                logId,
                SecurityUtils.getAuthenticatedUserId(),
                HelperRequestUtil.getApiPartnerInfo(),
                ids);

        var result = fileService.findFilesByIds(logId, ids);

        ResponseEntity<ApiResponse<List<FileDTO>>> response = ResponseEntity
                .ok()
                .body(new ApiResponse<>(responseMessageService.findByApiStatus(logId, ApiStatus.OK), result));

        log.debug("Response -- logId: {}, data: {}", logId, response);
        return response;
    }
}

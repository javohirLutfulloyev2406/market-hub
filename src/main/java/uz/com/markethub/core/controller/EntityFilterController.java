package uz.com.markethub.core.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import uz.com.markethub.core.dto.ApiResponse;
import uz.com.markethub.core.record.ApiStatus;
import uz.com.markethub.core.service.ApiResponseMessageService;
import uz.com.markethub.core.service.EntityFilterService;
import uz.com.markethub.core.util.HelperUtil;
import uz.com.markethub.core.util.PaginationUtil;
import uz.com.markethub.module.User.util.HelperRequestUtil;
import uz.com.markethub.security.util.SecurityUtils;


@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/entity-filter")
public class EntityFilterController {

    private final EntityFilterService entityFilterService;
    private final ApiResponseMessageService responseMessageService;

    @GetMapping
    @PreAuthorize("hasAuthority('ENTITY_FILTER')")
    public ResponseEntity<ApiResponse<?>> filter(@RequestParam String entity, @RequestParam MultiValueMap<String, String> filters, Pageable pageable) {
        Long logId = HelperUtil.generateLogId();
        log.debug("Request -- logId: {}, userId: {}, apiPartnerInfo: {}, findAll",
                logId,
                SecurityUtils.getAuthenticatedUserId(),
                HelperRequestUtil.getApiPartnerInfo());

        Page<?> result = entityFilterService.filter(entity, filters, pageable, logId);

        ResponseEntity<ApiResponse<?>> response = ResponseEntity
                .ok()
                .headers(PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), result))
                .body(new ApiResponse<>(
                        responseMessageService.findByApiStatus(logId, ApiStatus.OK),
                        result.getContent())
                );
        log.debug("Response -- logId: {}, data: {}", logId, response);
        return response;
    }
}

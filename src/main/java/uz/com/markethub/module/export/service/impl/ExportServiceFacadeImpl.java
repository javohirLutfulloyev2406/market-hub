package uz.com.markethub.module.export.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import uz.com.markethub.module.export.dto.ExportDTO;
import uz.com.markethub.module.export.service.ExportService;
import uz.com.markethub.module.export.service.ExportServiceFacade;
import uz.com.markethub.module.export.service.async.ExportAsyncService;


@Log4j2
@Service
@RequiredArgsConstructor
public class ExportServiceFacadeImpl implements ExportServiceFacade {

    private final ExportService exportService;
    private final ExportAsyncService exportAsyncService;

    @Override
    @Transactional
    public ExportDTO.Full createAndExportData(Long logId, ExportDTO dto, MultiValueMap<String, String> filters) {
        log.debug("Request -- logId: {}, createAndExportData dto: {}", logId, dto);

        ExportDTO.Full exportDTO = exportService.create(logId, dto);

        exportAsyncService.processExportInBackground(logId, exportDTO, filters);

        log.info("Export task started async -- logId: {}, exportId: {}", logId, exportDTO.getId());
        return exportDTO;
    }
    @Override
    @Transactional
    public ExportDTO.Full createAndExportData(Long logId, ExportDTO dto) {
        log.debug("Request -- logId: {}, createAndExportData dto: {}", logId, dto);

        ExportDTO.Full exportDTO = exportService.create(logId, dto);

        exportAsyncService.processExportInBackground(logId, exportDTO);

        log.info("Export task started async -- logId: {}, exportId: {}", logId, exportDTO.getId());
        return exportDTO;
    }
}

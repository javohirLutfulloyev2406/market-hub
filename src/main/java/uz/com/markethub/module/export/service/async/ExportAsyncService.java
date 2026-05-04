package uz.com.markethub.module.export.service.async;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import uz.com.markethub.core.enums.FileExtension;
import uz.com.markethub.core.enums.FilePrefix;
import uz.com.markethub.module.export.constants.ExportStatus;
import uz.com.markethub.module.export.dto.ExportDTO;
import uz.com.markethub.module.export.factory.ExportExcelStrategyFactory;
import uz.com.markethub.module.export.service.ExportService;
import uz.com.markethub.module.fileSystem.domain.FileEntity;
import uz.com.markethub.module.fileSystem.service.FileService;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExportAsyncService {

    private final ExportExcelStrategyFactory exportExcelStrategyFactory;
    private final FileService fileService;
    private final ExportService exportService;

    @Async("exportExecutor")
    public void processExportInBackground(Long logId, ExportDTO.Full exportDTO, MultiValueMap<String, String> filters) {
        try {
            log.info("Async export started -- logId: {}, exportId: {}", logId, exportDTO.getId());

            var exporter = exportExcelStrategyFactory.getExporter(exportDTO.getReportType());
            if (exporter == null) {
                throw new IllegalArgumentException("Unsupported report type: " + exportDTO.getReportType());
            }

            byte[] fileArray = exporter.exportData(exportDTO, logId, filters);

            FileEntity fileEntity = fileService.uploadFile(
                    logId,
                    FilePrefix.REPORT,
                    FileExtension.EXCEL,
                    fileArray
            );

            exportService.setParams(
                    logId,
                    exportDTO.getId(),
                    fileEntity,
                    ExportStatus.COMPLETED,
                    LocalDateTime.now(),
                    null
            );

            log.info("Async export completed successfully -- logId: {}, exportId: {}", logId, exportDTO.getId());

        } catch (Exception e) {
            log.error("Async export failed -- logId: {}, exportId: {}", logId, exportDTO.getId(), e);
            exportService.setParams(
                    logId,
                    exportDTO.getId(),
                    null,
                    ExportStatus.FAILED,
                    LocalDateTime.now(),
                    e.getMessage()
            );
        }
    }
    @Async("exportExecutor")
    public void processExportInBackground(Long logId, ExportDTO.Full exportDTO) {
        try {
            log.info("Async export started -- logId: {}, exportId: {}", logId, exportDTO.getId());

            var exporter = exportExcelStrategyFactory.getExporter(exportDTO.getReportType());
            if (exporter == null) {
                throw new IllegalArgumentException("Unsupported report type: " + exportDTO.getReportType());
            }

            byte[] fileArray = exporter.exportData(exportDTO, logId, null);

            FileEntity fileEntity = fileService.uploadFile(
                    logId,
                    FilePrefix.REPORT,
                    FileExtension.EXCEL,
                    fileArray
            );

            exportService.setParams(
                    logId,
                    exportDTO.getId(),
                    fileEntity,
                    ExportStatus.COMPLETED,
                    LocalDateTime.now(),
                    null
            );

            log.info("Async export completed successfully -- logId: {}, exportId: {}", logId, exportDTO.getId());

        } catch (Exception e) {
            log.error("Async export failed -- logId: {}, exportId: {}", logId, exportDTO.getId(), e);
            exportService.setParams(
                    logId,
                    exportDTO.getId(),
                    null,
                    ExportStatus.FAILED,
                    LocalDateTime.now(),
                    e.getMessage()
            );
        }
    }
}

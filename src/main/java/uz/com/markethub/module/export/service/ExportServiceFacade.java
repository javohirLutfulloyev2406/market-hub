package uz.com.markethub.module.export.service;

import org.springframework.util.MultiValueMap;
import uz.com.markethub.module.export.dto.ExportDTO;

public interface ExportServiceFacade {
    ExportDTO.Full createAndExportData(Long logId, ExportDTO dto, MultiValueMap<String, String> filters);
    ExportDTO.Full createAndExportData(Long logId, ExportDTO dto);
}

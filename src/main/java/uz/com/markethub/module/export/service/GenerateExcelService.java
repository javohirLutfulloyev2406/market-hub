package uz.com.markethub.module.export.service;

import org.springframework.util.MultiValueMap;
import uz.com.markethub.module.export.constants.ReportType;
import uz.com.markethub.module.export.dto.ExportDTO;


public interface GenerateExcelService {

    byte[] exportData(ExportDTO.Full exportDTO, Long logId, MultiValueMap<String, String> filters);
    byte[] exportData(ExportDTO.Full exportDTO, Long logId);
    ReportType getReportType();

}

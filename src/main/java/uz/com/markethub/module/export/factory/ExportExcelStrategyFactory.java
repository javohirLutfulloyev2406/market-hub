package uz.com.markethub.module.export.factory;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uz.com.markethub.module.export.constants.ReportType;
import uz.com.markethub.module.export.service.GenerateExcelService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ExportExcelStrategyFactory {

    private final List<GenerateExcelService> exporterList;
    private Map<ReportType, GenerateExcelService> exporters;

    @PostConstruct
    void init() {
        this.exporters = exporterList.stream()
                .collect(Collectors.toMap(GenerateExcelService::getReportType, e -> e));
    }

    public GenerateExcelService getExporter(ReportType type) {
        GenerateExcelService exporter = exporters.get(type);
        if (exporter == null) {
            throw new IllegalArgumentException("Unsupported report type: " + type);
        }
        return exporter;
    }
}

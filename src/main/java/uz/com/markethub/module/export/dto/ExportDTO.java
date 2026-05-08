package uz.com.markethub.module.export.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import uz.com.markethub.module.User.domain.UserEntity;
import uz.com.markethub.module.export.constants.ExportStatus;
import uz.com.markethub.module.export.constants.ReportType;
import uz.com.markethub.module.export.domain.ExportEntity;
import uz.com.markethub.module.fileSystem.dto.FileDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExportDTO {

    private Long id;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    private ExportStatus status;

    @NotNull
    private ReportType reportType;

    private LocalDateTime completedAt;


    @Getter
    @Setter
    @SuperBuilder
    @NoArgsConstructor
    @ToString(callSuper = true)
    public static class Full extends ExportDTO {
        private FileDTO.Full file;
        private String errorMessage;
        private Long createdAt;
        private Long updatedAt;
        private String createdBy;
        private String updatedBy;
    }


    public ExportEntity map2Entity(UserEntity userEntity) {
        return ExportEntity.builder()
                .startDate(this.getStartDate())
                .endDate(this.getEndDate())
                .user(userEntity)
                .status(ExportStatus.IN_PROGRESS)
                .reportType(this.getReportType())
                .build();
    }

    public void set2Entity(ExportEntity entity) {
        if (entity == null) return;
        entity.setStartDate(this.getStartDate());
        entity.setEndDate(this.getEndDate());
        entity.setStatus(this.getStatus());
        entity.setReportType(this.getReportType());
        entity.setCompletedAt(this.getCompletedAt() != null ? this.getCompletedAt() : LocalDateTime.now());
    }

}

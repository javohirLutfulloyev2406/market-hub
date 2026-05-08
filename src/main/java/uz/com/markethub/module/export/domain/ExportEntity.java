package uz.com.markethub.module.export.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import uz.com.markethub.core.config.listener.HistoryListener;
import uz.com.markethub.core.domain.AbstractAuditEntity;
import uz.com.markethub.core.fileStorage.MinioStorageService;
import uz.com.markethub.core.util.DateUtils;
import uz.com.markethub.module.User.domain.UserEntity;
import uz.com.markethub.module.export.constants.ExportStatus;
import uz.com.markethub.module.export.constants.ReportType;
import uz.com.markethub.module.export.dto.ExportDTO;
import uz.com.markethub.module.fileSystem.domain.FileEntity;


import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "exports", schema = "excel_sch")
@Setter
@Getter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(HistoryListener.class)
public class ExportEntity extends AbstractAuditEntity<Long> implements Serializable {

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private ExportStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "report_type", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private ReportType reportType;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id")
    private FileEntity file;

    public ExportDTO.Full map2FullDTO(MinioStorageService storageService) {
        return ExportDTO.Full.builder()
                .id(this.getId())
                .startDate(this.getStartDate())
                .endDate(this.getEndDate())
                .status(this.getStatus())
                .errorMessage(this.getErrorMessage())
                .reportType(this.getReportType())
                .completedAt(this.getCompletedAt())
                .file(this.file != null ? this.file.map2FullDTO(storageService) : null)
                .createdAt(Objects.nonNull(super.getCreatedAt()) ? DateUtils.convertToMillis(super.getCreatedAt()) : null)
                .updatedAt(Objects.nonNull(super.getUpdatedAt()) ? DateUtils.convertToMillis(super.getUpdatedAt()) : null)
                .createdBy(this.getCreatedBy())
                .updatedBy(this.getUpdatedBy())
                .build();
    }

    public ExportDTO map2DTO() {
        return ExportDTO.builder()
                .id(this.getId())
                .startDate(this.getStartDate())
                .endDate(this.getEndDate())
                .status(this.getStatus())
                .reportType(this.getReportType())
                .completedAt(this.getCompletedAt())
                .build();
    }
}

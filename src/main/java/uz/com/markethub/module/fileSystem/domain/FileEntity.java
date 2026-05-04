package uz.com.markethub.module.fileSystem.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;
import uz.com.markethub.core.config.listener.HistoryListener;
import uz.com.markethub.core.domain.AbstractAuditEntity;
import uz.com.markethub.core.fileStorage.MinioStorageService;
import uz.com.markethub.core.util.DateUtils;
import uz.com.markethub.module.fileSystem.dto.FileDTO;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "files", schema = "file_sch") //sch = schema
@Setter
@Getter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(HistoryListener.class)
public class FileEntity extends AbstractAuditEntity<Long> implements Serializable {

    @Column(name = "file_name", nullable = false)
    private String fileName;// original file name

    @Column(name = "file_path", nullable = false)
    private String filePath;       // serverdagi path

    @Column(name = "file_size", nullable = false)
    private Long fileSize;         // bytes

    @Column(name = "content_type", nullable = false)
    private String contentType;    // MIME type (image/png, application/pdf)

    public FileDTO.Full map2FullDTO(MinioStorageService storageService) {
        return FileDTO.Full.builder()
                .id(this.getId())
                .fileName(this.getFileName())
                .filePath(storageService.generatePresignedUrl(this.getFilePath()))
                .fileSize(this.getFileSize())
                .contentType(this.getContentType())
                .createdBy(super.getCreatedBy())
                .updatedBy(super.getUpdatedBy())
                .updatedAt(Objects.nonNull(super.getUpdatedAt()) ? DateUtils.convertToMillis(super.getUpdatedAt()) : null)
                .createdAt(Objects.nonNull(super.getCreatedAt()) ? DateUtils.convertToMillis(super.getCreatedAt()) : null)
                .build();
    }

    public FileDTO map2DTO() {
        return FileDTO.builder()
                .id(this.getId())
                .fileName(this.getFileName())
                .filePath(this.getFilePath())
                .fileSize(this.getFileSize())
                .contentType(this.getContentType())
                .build();
    }
}
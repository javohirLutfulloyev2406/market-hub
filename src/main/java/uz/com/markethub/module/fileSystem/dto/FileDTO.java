package uz.com.markethub.module.fileSystem.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
public class FileDTO {

    private Long id;
    private String fileName;       // original file name
    private String filePath;       // serverdagi path
    private Long fileSize;         // bytes
    private String contentType;    // MIME type (image/png, application/pdf)


    @Getter
    @Setter
    @ToString(callSuper = true)
    @SuperBuilder
    @NoArgsConstructor
    public static class Full extends FileDTO {

        private Long createdAt;
        private Long updatedAt;
        private String createdBy;
        private String updatedBy;

    }
}

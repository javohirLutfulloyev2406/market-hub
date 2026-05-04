package uz.com.markethub.module.settings.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import uz.com.markethub.module.settings.domain.ApiMessageEntity;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
public class ApiMessageDTO {
    private Long id;
    @NotEmpty
    private String key;
    @NotNull
    private Integer code;
    @NotEmpty
    private String messageUz;
    @NotEmpty
    private String messageEn;
    @NotEmpty
    private String messageRu;


    public ApiMessageEntity map2Entity() {
        return ApiMessageEntity
                .builder()
                .key(this.key)
                .code(this.code)
                .messageUz(this.messageUz)
                .messageEn(this.messageEn)
                .messageRu(this.messageRu)
                .build();
    }

    public void set2Entity(ApiMessageEntity entity) {
        entity.setKey(this.key);
        entity.setCode(this.code);
        entity.setMessageUz(this.messageUz);
        entity.setMessageEn(this.messageEn);
        entity.setMessageRu(this.messageRu);
    }

    @Getter
    @Setter
    @SuperBuilder
    @NoArgsConstructor
    @ToString(callSuper = true)
    public static class Full extends ApiMessageDTO {
        private Long createdAt;
        private Long updatedAt;
        private String createdBy;
        private String updatedBy;
    }

}

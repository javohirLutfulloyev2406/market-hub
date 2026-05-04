package uz.com.markethub.module.settings.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import uz.com.markethub.core.domain.AbstractAuditEntity;
import uz.com.markethub.core.util.DateUtils;
import uz.com.markethub.module.settings.dto.ApiMessageDTO;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "api_messages", schema = "setting")
@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ApiMessageEntity extends AbstractAuditEntity<Long> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "key", unique = true)
    private String key;

    @Column(name = "code", unique = true)
    private Integer code;

    @Column(name = "message_uz")
    private String messageUz;

    @Column(name = "message_en")
    private String messageEn;

    @Column(name = "message_ru")
    private String messageRu;

    public ApiMessageDTO map2DTO() {
        return ApiMessageDTO
                .builder()
                .id(this.id)
                .key(this.key)
                .code(this.code)
                .messageUz(this.messageUz)
                .messageEn(this.messageEn)
                .messageRu(this.messageRu)
                .build();
    }

    public ApiMessageDTO.Full map2FullDTO() {
        return ApiMessageDTO.Full
                .builder()
                .id(this.id)
                .key(this.key)
                .code(this.code)
                .messageUz(this.messageUz)
                .messageEn(this.messageEn)
                .messageRu(this.messageRu)
                .createdAt(Objects.nonNull(this.createdAt) ? DateUtils.convertToMillis(this.createdAt) : null)
                .createdBy(super.createdBy)
                .updatedAt(Objects.nonNull(this.updatedAt) ? DateUtils.convertToMillis(this.updatedAt) : null)
                .updatedBy(super.updatedBy)
                .build();
    }
}
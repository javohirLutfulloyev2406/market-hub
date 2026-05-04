package uz.com.markethub.module.User.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import uz.com.markethub.core.config.listener.HistoryListener;
import uz.com.markethub.core.domain.AbstractAuditEntity;
import uz.com.markethub.core.util.DateUtils;
import uz.com.markethub.module.User.dto.ApiPartnerDTO;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "api_partners", schema = "user_sch") //sch = schema
@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(HistoryListener.class)
public class ApiPartnerEntity extends AbstractAuditEntity<Long> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "name")
    private String name;

    @Column(name = "code", unique = true)
    private String code;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "version")
    private String version;

    @Column(name = "description")
    private String description;

    public static ApiPartnerEntity mapFromId(Long id) {
        return ApiPartnerEntity
                .builder()
                .id(id)
                .build();
    }

    public ApiPartnerDTO map2DTO() {
        return ApiPartnerDTO
                .builder()
                .id(this.id)
                .name(this.name)
                .build();
    }

    public ApiPartnerDTO.Full map2FullDTO() {
        return ApiPartnerDTO.Full
                .builder()
                .id(this.id)
                .name(this.name)
                .code(this.code)
                .enabled(this.enabled)
                .version(this.version)
                .description(this.description)
                .createdAt(Objects.nonNull(this.createdAt) ? DateUtils.convertToMillis(this.createdAt) : null)
                .createdBy(super.createdBy)
                .updatedAt(Objects.nonNull(this.updatedAt) ? DateUtils.convertToMillis(this.updatedAt) : null)
                .updatedBy(super.updatedBy)
                .build();
    }
}

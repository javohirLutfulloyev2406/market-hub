package uz.com.markethub.module.User.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import uz.com.markethub.core.config.listener.HistoryListener;
import uz.com.markethub.core.domain.AbstractAuditEntity;
import uz.com.markethub.core.util.DateUtils;
import uz.com.markethub.module.User.domain.enums.PermissionAction;
import uz.com.markethub.module.User.dto.PermissionDTO;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "permissions", schema = "user_sch") //sch = schema
@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(HistoryListener.class)
public class PermissionEntity extends AbstractAuditEntity<Long> implements Serializable, GrantedAuthority {
    private static final long serialVersionUID = 1L;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "action", nullable = false)
    @Enumerated(EnumType.STRING)
    private PermissionAction action;

    @Column(name = "description")
    private String description;

    @Override
    public String getAuthority() {
        return this.code;
    }

    public PermissionDTO map2DTO() {
        return PermissionDTO.builder()
                .id(super.id)
                .action(this.action)
                .code(this.code)
                .description(this.description)
                .build();
    }

    public PermissionDTO.Full map2FullDTO() {
        return PermissionDTO.Full
                .builder()
                .id(super.id)
                .code(this.code)
                .action(this.action)
                .description(this.description)
                .createdAt(Objects.nonNull(this.createdAt) ? DateUtils.convertToMillis(this.createdAt) : null)
                .createdBy(super.createdBy)
                .updatedAt(Objects.nonNull(this.updatedAt) ? DateUtils.convertToMillis(this.updatedAt) : null)
                .updatedBy(super.updatedBy)
                .build();
    }

    public static PermissionEntity mapFromId(Long id) {
        if (id == null) return null;
        return PermissionEntity.builder()
                .id(id)
                .build();
    }
}

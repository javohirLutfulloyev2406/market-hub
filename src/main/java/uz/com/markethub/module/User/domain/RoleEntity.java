package uz.com.markethub.module.User.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import uz.com.markethub.core.config.listener.HistoryListener;
import uz.com.markethub.core.domain.AbstractAuditEntity;
import uz.com.markethub.core.enums.AcceptLanguage;
import uz.com.markethub.core.util.DateUtils;
import uz.com.markethub.module.User.domain.enums.RoleType;
import uz.com.markethub.module.User.dto.PermissionDTO;
import uz.com.markethub.module.User.dto.RoleDTO;
import uz.com.markethub.module.User.util.HelperRequestUtil;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "roles", schema = "user_sch") //sch = schema
@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(HistoryListener.class)
public class RoleEntity extends AbstractAuditEntity<Long> implements Serializable {

    /**
     * The main purpose of this column is to write words in the Latin alphabet and in Uzbek (alphabet == Latin && language == uzbek)
     */
    @Column(name = "name_uz")
    private String nameUz;

    /**
     * The main purpose of this column is to write words in the Cyrillic  alphabet and in Uzbek  (alphabet == Cyrillic && language == uzbek)
     */
    @Column(name = "name_en")
    private String nameEn;

    /**
     * The main purpose of this column is to write words in the Cyrillic alphabet and in Russian (alphabet == Cyrillic && language == Russian)
     */
    @Column(name = "name_ru")
    private String nameRu;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_type")
    private RoleType roleType;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "roles_permissions",
            schema = "user_sch",
            joinColumns = {@JoinColumn(name = "role_id")},
            inverseJoinColumns = {@JoinColumn(name = "permission_id")})
    @Builder.Default
    Set<PermissionEntity> permissions = new LinkedHashSet<>();

    public static RoleEntity mapFromId(Long id) {
        if (Objects.isNull(id)) {
            return null;
        }

        return RoleEntity.builder()
                .id(id)
                .build();
    }

    public RoleDTO map2DTO() {
        return RoleDTO.builder()
                .id(super.id)
                .name(getNameByLanguage())
                .roleType(this.roleType)
                .description(this.description)
                .build();
    }

    public RoleDTO.Full map2FullDTO() {
        List<PermissionDTO.Full> permissions = this.permissions
                .stream()
                .map(PermissionEntity::map2FullDTO)
                .collect(Collectors.toList());

        return RoleDTO.Full.builder()
                .id(super.id)
                .nameUz(this.nameUz)
                .nameEn(this.nameEn)
                .nameRu(this.nameRu)
                .description(this.description)
                .roleType(this.roleType)
                .createdAt(Objects.nonNull(super.createdAt) ? DateUtils.convertToMillis(this.createdAt) : null)
                .createdBy(super.createdBy)
                .updatedAt(Objects.nonNull(super.updatedAt) ? DateUtils.convertToMillis(this.updatedAt) : null)
                .updatedBy(super.updatedBy)
                .permissions(permissions)
                .build();
    }

    private String getNameByLanguage() {
        AcceptLanguage acceptLanguage = HelperRequestUtil.getAcceptLanguage();
        if (acceptLanguage == null) return this.getNameUz();
        return switch (acceptLanguage) {
            case RU -> this.getNameRu();
            case EN -> this.getNameEn();
            default -> this.getNameUz();
        };
    }
}

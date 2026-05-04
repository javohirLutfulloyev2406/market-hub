package uz.com.markethub.module.User.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import uz.com.markethub.module.User.domain.PermissionEntity;
import uz.com.markethub.module.User.domain.RoleEntity;
import uz.com.markethub.module.User.domain.enums.RoleType;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleDTO {
    private Long id;
    private String name;
    private String description;
    @NotNull
    private RoleType roleType;

    @Getter
    @Setter
    @SuperBuilder
    @NoArgsConstructor
    @ToString(callSuper = true)
    public static class Full extends RoleDTO {
        private String nameUz;
        private String nameEn;
        private String nameRu;
        private List<PermissionDTO.Full> permissions;
        private Long createdAt;
        private String createdBy;
        private Long updatedAt;
        private String updatedBy;
    }

    @Getter
    @Setter
    @SuperBuilder
    @NoArgsConstructor
    @ToString(callSuper = true)
    public static class CreateOrUpdate extends RoleDTO {

        @NotBlank
        private String nameUz;
        @NotBlank
        private String nameEn;
        @NotBlank
        private String nameRu;

        @NotEmpty
        private List<Long> permissionIds;

        public RoleEntity map2Entity(Set<PermissionEntity> permissions) {
            return RoleEntity.builder()
                    .nameUz(this.getNameUz())
                    .nameEn(this.getNameEn())
                    .nameRu(this.getNameRu())
                    .roleType(this.getRoleType())
                    .description(this.getDescription())
                    .permissions(permissions)
                    .build();
        }

        public void set2Entity(RoleEntity entity) {
            entity.setNameUz(this.getNameUz());
            entity.setNameEn(this.getNameEn());
            entity.setNameRu(this.getNameRu());
            entity.setDescription(this.getDescription());
            entity.setRoleType(this.getRoleType());

            if (this.getPermissionIds() != null) {
                Set<PermissionEntity> newPermissions = this.getPermissionIds().stream()
                        .map(PermissionEntity::mapFromId)
                        .collect(Collectors.toSet());
                entity.getPermissions().clear();
                entity.getPermissions().addAll(newPermissions);
            }
        }
    }
}

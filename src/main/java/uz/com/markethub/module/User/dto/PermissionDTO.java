package uz.com.markethub.module.User.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import uz.com.markethub.module.User.domain.PermissionEntity;
import uz.com.markethub.module.User.domain.enums.PermissionAction;


import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
public class PermissionDTO {
    private Long id;
    private String code;
    private PermissionAction action;
    private String description;

    public PermissionEntity map2Entity() {
        return PermissionEntity
                .builder()
                .id(this.id)
                .code(this.code)
                .action(this.action)
                .description(this.description)
                .build();
    }

    @Getter
    @Setter
    @SuperBuilder
    @NoArgsConstructor
    public static class Full extends PermissionDTO {
        private Long createdAt;
        private Long updatedAt;
        private String createdBy;
        private String updatedBy;

        public void set2Entity(PermissionEntity entity) {
            entity.setAction(super.getAction());
            entity.setCode(super.code);
            entity.setDescription(super.getDescription());
        }
    }
}


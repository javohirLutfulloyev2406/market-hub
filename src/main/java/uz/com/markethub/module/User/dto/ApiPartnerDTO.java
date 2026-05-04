package uz.com.markethub.module.User.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import uz.com.markethub.module.User.domain.ApiPartnerEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
public class ApiPartnerDTO {
    private Long id;
    private String name;

    public ApiPartnerEntity map2Entity() {
        return ApiPartnerEntity
                .builder()
                .id(this.id)
                .name(this.name)
                .build();
    }

    @Getter
    @Setter
    @SuperBuilder
    @NoArgsConstructor
    @ToString(callSuper = true)
    public static class Full extends ApiPartnerDTO {
        private String code;
        private boolean enabled;
        private String version;
        private String description;
        private String createdBy;
        private String updatedBy;
        private Long createdAt;
        private Long updatedAt;

        @Override
        public ApiPartnerEntity map2Entity() {

            return ApiPartnerEntity.builder()
                    .id(super.getId())
                    .name(super.getName())
                    .code(this.code)
                    .enabled(this.enabled)
                    .version(this.version)
                    .description(description)
                    .build();

        }

        public void set2Entity(ApiPartnerEntity entity) {
            entity.setName(super.getName());
            entity.setCode(this.code);
            entity.setEnabled(this.enabled);
            entity.setVersion(this.version);
            entity.setDescription(description);
        }
    }
}

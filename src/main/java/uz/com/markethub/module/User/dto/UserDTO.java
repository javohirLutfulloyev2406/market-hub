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
import uz.com.markethub.module.User.domain.RoleEntity;
import uz.com.markethub.module.User.domain.UserEntity;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@SuperBuilder
@ToString
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String username;

    @NotNull
    private Boolean enabled;

    private String telegramChatId;


    @Getter
    @Setter
    @SuperBuilder
    @NoArgsConstructor
    @ToString(callSuper = true)
    public static class Full extends UserDTO {
        private Set<RoleDTO.Full> roles;
        private Long createdAt;
        private Long updatedAt;
        private String createdBy;
        private String updatedBy;
    }

    @Getter
    @Setter
    @ToString
    @SuperBuilder
    @NoArgsConstructor
    public static class CreateOrUpdate extends UserDTO {
        private String password;
        @NotEmpty
        private List<Long> roleIds;

        public UserEntity map2Entity(Set<RoleEntity> roles, String encodedPassword) {
            return UserEntity.builder()
                    .name(this.getName())
                    .username(this.getUsername())
                    .password(encodedPassword)
                    .enabled(this.getEnabled())
                    .accountNonExpired(true)
                    .accountNonLocked(true)
                    .telegramChatId(this.getTelegramChatId())
                    .roles(roles)
                    .build();
        }

        public void set2Entity(UserEntity entity) {
            entity.setName(this.getName());
            entity.setUsername(this.getUsername());
            entity.setEnabled(this.getEnabled());
            entity.setTelegramChatId(this.getTelegramChatId());
            if (this.getRoleIds() != null) {
                Set<RoleEntity> newRoles = this.getRoleIds().stream()
                        .map(RoleEntity::mapFromId)
                        .collect(Collectors.toSet());
                entity.getRoles().clear();
                entity.getRoles().addAll(newRoles);
            }
        }

    }
}

// change password
// admin change password users
//
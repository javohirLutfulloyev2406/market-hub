package uz.com.markethub.module.User.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.com.markethub.core.config.listener.HistoryListener;
import uz.com.markethub.core.domain.AbstractAuditEntity;
import uz.com.markethub.core.util.DateUtils;
import uz.com.markethub.module.User.dto.UserDTO;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users", schema = "user_sch") //sch = schema
@Setter
@Getter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(HistoryListener.class)
public class UserEntity extends AbstractAuditEntity<Long> implements UserDetails, Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "name", nullable = false)
    private String name;
    /**
     * Authorization user via username and password
     */
    @Column(name = "username", unique = true, updatable = false, nullable = false)
    private String username;

    @Column(name = "password_hash", length = 60, nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "users_roles",
            schema = "user_sch",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}
    )
    @Builder.Default
    private Set<RoleEntity> roles = new LinkedHashSet<>();

    /**
     * Активирована ли учетная запись
     */
    @Column(name = "account_non_expired")
    private boolean accountNonExpired;

    /**
     * Не заблокирована ли учетная запись пользователя
     */
    @Column(name = "account_non_locked")
    private boolean accountNonLocked;

    /**
     * Включена ли учетная запись
     */
    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @Column(name = "telegram_chat_id")
    private String telegramChatId;

    public UserDTO map2DTO() {
        return UserDTO.builder()
                .id(super.id)
                .name(this.name)
                .username(this.username)
                .enabled(this.enabled)
                .telegramChatId(this.telegramChatId)
                .build();
    }

    public UserDTO.Full map2FullDTO() {
        return UserDTO.Full.builder()
                .id(super.id)
                .name(this.name)
                .username(this.username)
                .enabled(this.enabled)
                .telegramChatId(this.telegramChatId)
                .createdAt(Objects.nonNull(this.getCreatedAt()) ? DateUtils.convertToMillis(this.getCreatedAt()) : null)
                .createdBy(this.getCreatedBy())
                .updatedAt(Objects.nonNull(this.getUpdatedAt()) ? DateUtils.convertToMillis(this.getUpdatedAt()) : null)
                .updatedBy(this.getUpdatedBy())
                .roles(this.roles.stream()
                        .map(RoleEntity::map2FullDTO)
                        .collect(Collectors.toSet()))
                .build();
    }

    // ===== SPRING SECURITY METHODS =====

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        var authorities = new LinkedHashSet<GrantedAuthority>();

        if (!roles.isEmpty()) {
            roles.stream()
                    .flatMap(role -> role.getPermissions().stream())
                    .map(permission -> new SimpleGrantedAuthority(permission.getCode()))
                    .forEach(authorities::add);
        }

        return authorities;
    }


    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}

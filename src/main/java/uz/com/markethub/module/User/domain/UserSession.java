package uz.com.markethub.module.User.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import uz.com.markethub.core.domain.AbstractAuditEntity;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_sessions", schema = "user_sch") //sch = schema
@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserSession extends AbstractAuditEntity<Long> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "access_token", nullable = false)
    private String accessToken;

    @Column(name = "is_active")
    private Boolean active;

    @Column(name = "expire_date")
    private LocalDateTime expireDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

}

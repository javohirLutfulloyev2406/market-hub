package uz.com.markethub.core.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "histories", schema = "history")
@EntityListeners(AuditingEntityListener.class)
public class HistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator", allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
    protected Long id;

    @Column(name = "entity_id", updatable = false)
    private Long entityId;

    @NotEmpty
    @Column(name = "entity", updatable = false, nullable = false)
    private String entity;

    @NotEmpty
    @Column(name = "operation", updatable = false, nullable = false)
    private String operation;

    @NotEmpty
    @Column(name = "payload", columnDefinition = "text")
    private String payload;

    @CreatedBy
    @Column(name = "created_by", updatable = false, nullable = false)
    protected String createdBy;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false, nullable = false)
    protected LocalDateTime createdAt;

}

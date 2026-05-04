package uz.com.markethub.core.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class AbstractBaseEntity<ID extends Long> implements Persistable<ID>, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator", allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
    protected ID id;

    @Builder.Default
    @Column(name = "deleted")
    protected boolean deleted = false;

    @Transient
    @Override
    public boolean isNew() {
        return null == getId();
    }
}

package uz.com.markethub.core.config.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import uz.com.markethub.core.domain.AbstractBaseEntity;
import uz.com.markethub.core.domain.HistoryEntity;
import uz.com.markethub.core.service.HistoryService;
import uz.com.markethub.core.util.AutowireHelper;
import uz.com.markethub.core.util.HelperUtil;

@Log4j2
@Component
public class HistoryListener {
    private final String OPERATION_CREATE = "CREATE";
    private final String OPERATION_UPDATE = "UPDATE";
    private final String OPERATION_DELETE = "DELETE";

    @Autowired
    @Lazy
    HistoryService service;

    @PrePersist
    @Transactional
    public void beforeCreate(Object object) throws JsonProcessingException {
        AutowireHelper.autowire(this, this.service);

        HistoryEntity historyEntity = create(object, OPERATION_CREATE);

        service.create(historyEntity);
    }

    @PreUpdate
    @Transactional
    protected void beforeUpdate(Object object) throws JsonProcessingException {
        AutowireHelper.autowire(this, this.service);

        String operation;

        if (object instanceof AbstractBaseEntity<?> base && base.isDeleted()) {
            operation = OPERATION_DELETE;
        } else {
            operation = OPERATION_UPDATE;
        }

        HistoryEntity historyEntity = create(object, operation);

        service.create(historyEntity);
    }

    @PreRemove
    @Transactional
    protected void beforeRemove(Object object) throws JsonProcessingException {
        AutowireHelper.autowire(this, this.service);

        HistoryEntity historyEntity = create(object, OPERATION_DELETE);

        service.create(historyEntity);
    }

    private HistoryEntity create(Object entity, String operation) throws JsonProcessingException {
        Long entityId = null;
        if (entity instanceof AbstractBaseEntity<?> base) {
            entityId = base.getId();
        }

        return HistoryEntity
                .builder()
                .entityId(entityId)
                .entity(entity.getClass().getSimpleName())
                .operation(operation)
                .payload(HelperUtil.object2JSON(entity))
                .build();
    }
}


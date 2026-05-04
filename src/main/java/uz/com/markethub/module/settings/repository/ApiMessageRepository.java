package uz.com.markethub.module.settings.repository;

import org.springframework.stereotype.Repository;
import uz.com.markethub.core.repository.GenericRepository;
import uz.com.markethub.module.settings.domain.ApiMessageEntity;

import java.util.Optional;

@Repository
public interface ApiMessageRepository extends GenericRepository<ApiMessageEntity,Long> {

    Optional<ApiMessageEntity> findByKey(String key);
}

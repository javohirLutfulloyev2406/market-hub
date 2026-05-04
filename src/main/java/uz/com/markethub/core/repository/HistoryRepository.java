package uz.com.markethub.core.repository;

import org.springframework.stereotype.Repository;
import uz.com.markethub.core.domain.HistoryEntity;


@Repository
public interface HistoryRepository extends GenericRepository<HistoryEntity, Long> {
}

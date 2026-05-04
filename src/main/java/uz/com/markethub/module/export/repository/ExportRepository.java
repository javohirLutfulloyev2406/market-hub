package uz.com.markethub.module.export.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import uz.com.markethub.core.repository.GenericRepository;
import uz.com.markethub.module.User.domain.UserEntity;
import uz.com.markethub.module.export.domain.ExportEntity;


import java.util.List;

@Repository
public interface ExportRepository extends GenericRepository<ExportEntity, Long> {

    Page<ExportEntity> findAllByUser(UserEntity user, Pageable pageable);
    List<ExportEntity> findAllByUser(UserEntity user);

}

package uz.com.markethub.module.User.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.com.markethub.core.repository.GenericRepository;
import uz.com.markethub.module.User.domain.RoleEntity;

import java.util.List;

@Repository
public interface RoleRepository extends GenericRepository<RoleEntity, Long> {

    @Query("SELECT r FROM RoleEntity r WHERE r.id IN :ids AND r.deleted = false")
    List<RoleEntity> findAllByIdIn(@Param("ids") List<Long> ids);

}

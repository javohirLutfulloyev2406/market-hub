package uz.com.markethub.module.User.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.com.markethub.core.repository.GenericRepository;
import uz.com.markethub.module.User.domain.UserEntity;
import uz.com.markethub.module.User.domain.enums.RoleType;


import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends GenericRepository<UserEntity, Long> {

    @Query(value = "SELECT e FROM UserEntity e WHERE e.username = :username and e.deleted = false ")
    Optional<UserEntity> findOneByUsername(@Param("username") String username);

    Optional<UserEntity> findByUsername(String username);

    Page<UserEntity> findDistinctByRolesRoleTypeIn(List<RoleType> roleTypes, Pageable pageable);

    List<UserEntity> findDistinctByRolesRoleTypeIn(List<RoleType> roleTypes);

    Optional<UserEntity> findByTelegramChatId(String id);
}

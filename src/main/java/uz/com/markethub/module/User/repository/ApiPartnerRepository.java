package uz.com.markethub.module.User.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.com.markethub.core.repository.GenericRepository;
import uz.com.markethub.module.User.domain.ApiPartnerEntity;


import java.util.Optional;

@Repository
public interface ApiPartnerRepository extends GenericRepository<ApiPartnerEntity, Long> {
    @Query("SELECT d FROM ApiPartnerEntity d WHERE d.code = :code and d.deleted = false")
    Optional<ApiPartnerEntity> findByCode(@Param("code") String code);

    boolean existsByName(String name);

}

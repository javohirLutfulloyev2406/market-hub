package uz.com.markethub.module.product.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.com.markethub.core.repository.GenericRepository;
import uz.com.markethub.module.product.domain.ProductEntity;

import java.util.Optional;

@Repository
public interface ProductRepository extends GenericRepository<ProductEntity, Long> {

    @Query("SELECT p FROM ProductEntity p WHERE p.sku = :sku AND p.deleted = false")
    Optional<ProductEntity> findBySku(@Param("sku") String sku);

    boolean existsBySkuAndDeletedFalse(String sku);

    boolean existsBySkuAndIdNotAndDeletedFalse(String sku, Long id);
}
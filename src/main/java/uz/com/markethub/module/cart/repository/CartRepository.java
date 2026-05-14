package uz.com.markethub.module.cart.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.com.markethub.core.repository.GenericRepository;
import uz.com.markethub.module.cart.domain.CartEntity;

import java.util.Optional;

@Repository
public interface CartRepository extends GenericRepository<CartEntity, Long> {

    Optional<CartEntity> findByBuyerIdAndDeletedFalse(Long buyerId);

    @Query("SELECT c FROM CartEntity c LEFT JOIN FETCH c.items i LEFT JOIN FETCH i.product WHERE c.buyer.id = :buyerId AND c.deleted = false")
    Optional<CartEntity> findByBuyerIdWithItems(@Param("buyerId") Long buyerId);
}
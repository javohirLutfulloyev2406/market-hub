package uz.com.markethub.module.order.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.com.markethub.core.repository.GenericRepository;
import uz.com.markethub.module.order.domain.OrderItemEntity;

import java.util.Optional;

@Repository
public interface OrderItemRepository extends GenericRepository<OrderItemEntity, Long> {

    @Query("""
            SELECT oi FROM OrderItemEntity oi
            JOIN FETCH oi.order o
            JOIN FETCH o.buyer
            LEFT JOIN FETCH oi.product
            WHERE oi.id = :id AND oi.deleted = false
            """)
    Optional<OrderItemEntity> findByIdWithOrderAndProduct(@Param("id") Long id);
}
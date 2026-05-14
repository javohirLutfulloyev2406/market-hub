package uz.com.markethub.module.order.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.com.markethub.core.repository.GenericRepository;
import uz.com.markethub.module.order.domain.OrderEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends GenericRepository<OrderEntity, Long> {

    // Buyer: list all own orders (JOIN FETCH resolves N+1 on items and product)
    @Query("SELECT DISTINCT o FROM OrderEntity o LEFT JOIN FETCH o.items i LEFT JOIN FETCH i.product WHERE o.buyer.id = :buyerId AND o.deleted = false ORDER BY o.createdAt DESC")
    List<OrderEntity> findAllByBuyerIdWithItems(@Param("buyerId") Long buyerId);

    // Buyer: single order detail
    @Query("SELECT o FROM OrderEntity o LEFT JOIN FETCH o.items i LEFT JOIN FETCH i.product WHERE o.id = :id AND o.buyer.id = :buyerId AND o.deleted = false")
    Optional<OrderEntity> findByIdAndBuyerIdWithItems(@Param("id") Long id, @Param("buyerId") Long buyerId);

    // Seller: paginated list of orders containing their products
    @Query(value = "SELECT DISTINCT o FROM OrderEntity o JOIN o.items i WHERE i.sellerUsername = :sellerUsername AND o.deleted = false",
            countQuery = "SELECT COUNT(DISTINCT o) FROM OrderEntity o JOIN o.items i WHERE i.sellerUsername = :sellerUsername AND o.deleted = false")
    Page<OrderEntity> findAllBySellerUsername(@Param("sellerUsername") String sellerUsername, Pageable pageable);

    // Seller: single order detail — only exposes items that belong to this seller
    @Query("SELECT o FROM OrderEntity o JOIN FETCH o.items i LEFT JOIN FETCH i.product WHERE o.id = :id AND i.sellerUsername = :sellerUsername AND o.deleted = false")
    Optional<OrderEntity> findByIdAndSellerUsernameWithItems(@Param("id") Long id, @Param("sellerUsername") String sellerUsername);

    // Status update — lightweight fetch without collection join
    @Query("SELECT o FROM OrderEntity o WHERE o.id = :id AND o.deleted = false")
    Optional<OrderEntity> findByIdNotDeleted(@Param("id") Long id);
}
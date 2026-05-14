package uz.com.markethub.module.review.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.com.markethub.core.repository.GenericRepository;
import uz.com.markethub.module.review.domain.ReviewEntity;

@Repository
public interface ReviewRepository extends GenericRepository<ReviewEntity, Long> {

    boolean existsByOrderItemIdAndDeletedFalse(Long orderItemId);

    @Query("""
            SELECT COALESCE(AVG(r.rating), 0.0)
            FROM ReviewEntity r
            WHERE r.product.id = :productId AND r.deleted = false
            """)
    Double calculateAverageRatingByProductId(@Param("productId") Long productId);

    Page<ReviewEntity> findAllByProductIdAndDeletedFalse(Long productId, Pageable pageable);
}
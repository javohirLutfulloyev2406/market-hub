package uz.com.markethub.module.review.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import uz.com.markethub.core.config.listener.HistoryListener;
import uz.com.markethub.core.domain.AbstractAuditEntity;
import uz.com.markethub.core.util.DateUtils;
import uz.com.markethub.module.order.domain.OrderItemEntity;
import uz.com.markethub.module.product.domain.ProductEntity;
import uz.com.markethub.module.review.dto.ReviewDTO;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "reviews", schema = "review_sch")
@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(HistoryListener.class)
public class ReviewEntity extends AbstractAuditEntity<Long> implements Serializable {

    @Column(name = "rating", nullable = false)
    private Integer rating;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id", nullable = false, unique = true)
    private OrderItemEntity orderItem;

    public ReviewDTO.Full map2FullDTO() {
        return ReviewDTO.Full.builder()
                .id(super.id)
                .rating(this.rating)
                .comment(this.comment)
                .productId(Objects.nonNull(this.product) ? this.product.getId() : null)
                .orderItemId(Objects.nonNull(this.orderItem) ? this.orderItem.getId() : null)
                .createdBy(super.createdBy)
                .createdAt(Objects.nonNull(super.createdAt) ? DateUtils.convertToMillis(super.createdAt) : null)
                .build();
    }
}

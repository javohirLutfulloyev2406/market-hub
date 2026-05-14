package uz.com.markethub.module.order.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import uz.com.markethub.core.domain.AbstractAuditEntity;
import uz.com.markethub.module.order.dto.OrderDTO;
import uz.com.markethub.module.product.domain.ProductEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "order_items", schema = "product_sch")
@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemEntity extends AbstractAuditEntity<Long> implements Serializable {

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @Column(name = "product_name_snapshot", nullable = false)
    private String productNameSnapshot;

    @Column(name = "product_sku_snapshot")
    private String productSkuSnapshot;

    @Column(name = "seller_username", nullable = false)
    private String sellerUsername;

    @Column(name = "price_at_purchase", nullable = false, precision = 19, scale = 2)
    private BigDecimal priceAtPurchase;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    public OrderDTO.Item map2DTO() {
        return OrderDTO.Item.builder()
                .id(super.id)
                .productId(Objects.nonNull(this.product) ? this.product.getId() : null)
                .productNameSnapshot(this.productNameSnapshot)
                .productSkuSnapshot(this.productSkuSnapshot)
                .sellerUsername(this.sellerUsername)
                .priceAtPurchase(this.priceAtPurchase)
                .quantity(this.quantity)
                .build();
    }
}
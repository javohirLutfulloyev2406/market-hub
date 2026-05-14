package uz.com.markethub.module.cart.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import uz.com.markethub.core.domain.AbstractAuditEntity;
import uz.com.markethub.module.cart.dto.CartDTO;
import uz.com.markethub.module.product.domain.ProductEntity;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "cart_items", schema = "product_sch")
@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemEntity extends AbstractAuditEntity<Long> implements Serializable {

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private CartEntity cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    public CartDTO.Item map2DTO() {
        return CartDTO.Item.builder()
                .id(super.id)
                .productId(Objects.nonNull(this.product) ? this.product.getId() : null)
                .productName(Objects.nonNull(this.product) ? this.product.getNameByLanguage() : null)
                .productPrice(Objects.nonNull(this.product) ? this.product.getPrice() : null)
                .productSku(Objects.nonNull(this.product) ? this.product.getSku() : null)
                .quantity(this.quantity)
                .build();
    }
}
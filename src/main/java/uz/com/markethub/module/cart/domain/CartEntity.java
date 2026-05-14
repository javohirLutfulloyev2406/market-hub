package uz.com.markethub.module.cart.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import uz.com.markethub.core.domain.AbstractAuditEntity;
import uz.com.markethub.core.util.DateUtils;
import uz.com.markethub.module.User.domain.UserEntity;
import uz.com.markethub.module.cart.dto.CartDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "carts", schema = "product_sch")
@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CartEntity extends AbstractAuditEntity<Long> implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = false, unique = true)
    private UserEntity buyer;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<CartItemEntity> items = new ArrayList<>();

    public CartDTO.Full map2FullDTO() {
        return CartDTO.Full.builder()
                .id(super.id)
                .buyerId(Objects.nonNull(this.buyer) ? this.buyer.getId() : null)
                .items(this.items.stream().map(CartItemEntity::map2DTO).toList())
                .createdAt(Objects.nonNull(super.createdAt) ? DateUtils.convertToMillis(super.createdAt) : null)
                .updatedAt(Objects.nonNull(super.updatedAt) ? DateUtils.convertToMillis(super.updatedAt) : null)
                .build();
    }
}
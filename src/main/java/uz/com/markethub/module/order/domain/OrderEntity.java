package uz.com.markethub.module.order.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import uz.com.markethub.core.domain.AbstractAuditEntity;
import uz.com.markethub.core.util.DateUtils;
import uz.com.markethub.module.User.domain.UserEntity;
import uz.com.markethub.module.order.constants.OrderStatus;
import uz.com.markethub.module.order.dto.OrderDTO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders", schema = "product_sch")
@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity extends AbstractAuditEntity<Long> implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = false)
    private UserEntity buyer;

    @Column(name = "shipping_address", nullable = false, columnDefinition = "TEXT")
    private String shippingAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private OrderStatus status = OrderStatus.PENDING;

    @Column(name = "total_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal totalAmount;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<OrderItemEntity> items = new ArrayList<>();

    public OrderDTO map2DTO() {
        return OrderDTO.builder()
                .id(super.id)
                .buyerId(Objects.nonNull(this.buyer) ? this.buyer.getId() : null)
                .status(this.status)
                .shippingAddress(this.shippingAddress)
                .totalAmount(this.totalAmount)
                .createdAt(Objects.nonNull(super.createdAt) ? DateUtils.convertToMillis(super.createdAt) : null)
                .build();
    }

    public OrderDTO.Full map2FullDTO() {
        return OrderDTO.Full.builder()
                .id(super.id)
                .buyerId(Objects.nonNull(this.buyer) ? this.buyer.getId() : null)
                .status(this.status)
                .shippingAddress(this.shippingAddress)
                .totalAmount(this.totalAmount)
                .items(this.items.stream().map(OrderItemEntity::map2DTO).toList())
                .createdAt(Objects.nonNull(super.createdAt) ? DateUtils.convertToMillis(super.createdAt) : null)
                .updatedAt(Objects.nonNull(super.updatedAt) ? DateUtils.convertToMillis(super.updatedAt) : null)
                .createdBy(super.createdBy)
                .build();
    }
}
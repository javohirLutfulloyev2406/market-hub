package uz.com.markethub.module.product.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import uz.com.markethub.core.config.listener.HistoryListener;
import uz.com.markethub.core.domain.AbstractAuditEntity;
import uz.com.markethub.core.enums.AcceptLanguage;
import uz.com.markethub.core.enums.BaseStatus;
import uz.com.markethub.core.util.DateUtils;
import uz.com.markethub.module.User.util.HelperRequestUtil;
import uz.com.markethub.module.category.domain.CategoryEntity;
import uz.com.markethub.module.product.dto.ProductDTO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "products", schema = "product_sch")
@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(HistoryListener.class)
public class ProductEntity extends AbstractAuditEntity<Long> implements Serializable {

    @Column(name = "name_uz")
    private String nameUz;

    @Column(name = "name_en")
    private String nameEn;

    @Column(name = "name_ru")
    private String nameRu;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "price", nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    @Version
    @Column(name = "version")
    private Long version;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "sku", unique = true)
    private String sku;

    @Builder.Default
    @Column(name = "average_rating", precision = 3, scale = 2)
    private Double averageRating = 0.0;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private BaseStatus status = BaseStatus.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    public ProductDTO map2DTO() {
        return ProductDTO.builder()
                .id(super.id)
                .name(getNameByLanguage())
                .price(this.price)
                .quantity(this.quantity)
                .sku(this.sku)
                .averageRating(this.averageRating)
                .status(this.status)
                .categoryId(Objects.nonNull(this.category) ? this.category.getId() : null)
                .build();
    }

    public ProductDTO.Full map2FullDTO() {
        return ProductDTO.Full.builder()
                .id(super.id)
                .nameUz(this.nameUz)
                .nameEn(this.nameEn)
                .nameRu(this.nameRu)
                .description(this.description)
                .price(this.price)
                .quantity(this.quantity)
                .sku(this.sku)
                .averageRating(this.averageRating)
                .status(this.status)
                .categoryId(Objects.nonNull(this.category) ? this.category.getId() : null)
                .categoryName(Objects.nonNull(this.category) ? this.category.getNameByLanguage() : null)
                .createdAt(Objects.nonNull(super.createdAt) ? DateUtils.convertToMillis(this.createdAt) : null)
                .createdBy(super.createdBy)
                .updatedAt(Objects.nonNull(super.updatedAt) ? DateUtils.convertToMillis(this.updatedAt) : null)
                .updatedBy(super.updatedBy)
                .build();
    }

    public String getNameByLanguage() {
        AcceptLanguage acceptLanguage = HelperRequestUtil.getAcceptLanguage();
        if (acceptLanguage == null) return this.nameUz;
        return switch (acceptLanguage) {
            case RU -> this.nameRu;
            case EN -> this.nameEn;
            default -> this.nameUz;
        };
    }
}
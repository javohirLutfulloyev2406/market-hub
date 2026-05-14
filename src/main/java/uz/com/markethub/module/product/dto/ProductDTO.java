package uz.com.markethub.module.product.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import uz.com.markethub.core.enums.BaseStatus;
import uz.com.markethub.module.category.domain.CategoryEntity;
import uz.com.markethub.module.product.domain.ProductEntity;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDTO implements Serializable {

    private Long id;
    private String name;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    @NotNull
    @Min(value = 0, message = "Quantity must be 0 or greater")
    private Integer quantity;

    private String sku;

    private Double averageRating;

    @NotNull
    private BaseStatus status;

    @NotNull
    private Long categoryId;

    @Getter
    @Setter
    @SuperBuilder
    @NoArgsConstructor
    @ToString(callSuper = true)
    public static class Full extends ProductDTO {
        private String nameUz;
        private String nameEn;
        private String nameRu;
        private String description;
        private String categoryName;
        private Long createdAt;
        private String createdBy;
        private Long updatedAt;
        private String updatedBy;
    }

    @Getter
    @Setter
    @SuperBuilder
    @NoArgsConstructor
    @ToString(callSuper = true)
    public static class CreateOrUpdate extends ProductDTO {

        @NotBlank
        private String nameUz;

        @NotBlank
        private String nameEn;

        @NotBlank
        private String nameRu;

        private String description;

        public ProductEntity map2Entity(CategoryEntity category) {
            return ProductEntity.builder()
                    .nameUz(this.nameUz)
                    .nameEn(this.nameEn)
                    .nameRu(this.nameRu)
                    .description(this.description)
                    .price(this.getPrice())
                    .quantity(this.getQuantity())
                    .sku(this.getSku())
                    .status(this.getStatus())
                    .category(category)
                    .build();
        }

        public void set2Entity(ProductEntity entity, CategoryEntity category) {
            entity.setNameUz(this.nameUz);
            entity.setNameEn(this.nameEn);
            entity.setNameRu(this.nameRu);
            entity.setDescription(this.description);
            entity.setPrice(this.getPrice());
            entity.setQuantity(this.getQuantity());
            entity.setSku(this.getSku());
            entity.setStatus(this.getStatus());
            entity.setCategory(category);
        }
    }
}
package uz.com.markethub.module.cart.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartDTO {

    private Long id;
    private Long buyerId;

    @Getter
    @Setter
    @SuperBuilder
    @NoArgsConstructor
    @ToString
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Item {
        private Long id;
        private Long productId;
        private String productName;
        private BigDecimal productPrice;
        private String productSku;
        private Integer quantity;
    }

    @Getter
    @Setter
    @SuperBuilder
    @NoArgsConstructor
    @ToString(callSuper = true)
    public static class Full extends CartDTO {
        private List<Item> items;
        private Long createdAt;
        private Long updatedAt;
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class AddOrUpdate {

        @NotNull
        private Long productId;

        @NotNull
        @Min(value = 1, message = "Quantity must be at least 1")
        private Integer quantity;
    }
}
package uz.com.markethub.module.order.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import uz.com.markethub.module.order.constants.OrderStatus;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDTO {

    private Long id;
    private Long buyerId;
    private OrderStatus status;
    private String shippingAddress;
    private BigDecimal totalAmount;
    private Long createdAt;

    @Getter
    @Setter
    @SuperBuilder
    @NoArgsConstructor
    @ToString
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Item {
        private Long id;
        private Long productId;
        private String productNameSnapshot;
        private String productSkuSnapshot;
        private String sellerUsername;
        private BigDecimal priceAtPurchase;
        private Integer quantity;
    }

    @Getter
    @Setter
    @SuperBuilder
    @NoArgsConstructor
    @ToString(callSuper = true)
    public static class Full extends OrderDTO {
        private List<Item> items;
        private Long updatedAt;
        private String createdBy;
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class UpdateStatus {

        @NotNull(message = "Status must not be null")
        private OrderStatus status;
    }
}
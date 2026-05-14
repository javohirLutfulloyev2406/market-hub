package uz.com.markethub.module.review.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class ReviewDTO {

    private Long id;
    private Integer rating;
    private String comment;
    private Long productId;
    private Long orderItemId;

    @Getter
    @Setter
    @SuperBuilder
    @NoArgsConstructor
    public static class Full extends ReviewDTO {
        private String createdBy;
        private Long createdAt;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class CreateRequest {

        @NotNull
        Long orderItemId;

        @NotNull
        @Min(1) @Max(5)
        Integer rating;

        @Size(max = 1000)
        String comment;
    }
}
package uz.com.markethub.module.review.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.com.markethub.module.review.dto.ReviewDTO;

public interface ReviewService {
    ReviewDTO.Full create(Long logId, ReviewDTO.CreateRequest request);
    Page<ReviewDTO.Full> findAllByProductId(Long logId, Long productId, Pageable pageable);
    void deleteById(Long logId, Long id);
}
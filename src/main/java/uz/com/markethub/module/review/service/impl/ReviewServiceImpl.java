package uz.com.markethub.module.review.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.com.markethub.core.exception.DuplicateValueException;
import uz.com.markethub.core.exception.ForbiddenException;
import uz.com.markethub.core.exception.InvalidParameterException;
import uz.com.markethub.core.exception.ResourceNotFoundException;
import uz.com.markethub.core.record.ApiStatus;
import uz.com.markethub.module.order.constants.OrderStatus;
import uz.com.markethub.module.order.domain.OrderEntity;
import uz.com.markethub.module.order.domain.OrderItemEntity;
import uz.com.markethub.module.order.repository.OrderItemRepository;
import uz.com.markethub.module.product.domain.ProductEntity;
import uz.com.markethub.module.product.repository.ProductRepository;
import uz.com.markethub.module.review.domain.ReviewEntity;
import uz.com.markethub.module.review.dto.ReviewDTO;
import uz.com.markethub.module.review.repository.ReviewRepository;
import uz.com.markethub.module.review.service.ReviewService;
import uz.com.markethub.security.util.SecurityUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public ReviewDTO.Full create(Long logId, ReviewDTO.CreateRequest request) {
        log.debug("Request -- logId: {}, create review: {}", logId, request);

        String currentUsername = SecurityUtils.getCurrentUserPrincipal().getUsername();

        OrderItemEntity orderItem = orderItemRepository.findByIdWithOrderAndProduct(request.getOrderItemId())
                .orElseThrow(() -> new ResourceNotFoundException(logId, request.getOrderItemId(), ApiStatus.ERR_ID_NOT_FOUND));

        OrderEntity order = orderItem.getOrder();
        if (!order.getBuyer().getUsername().equals(currentUsername)) {
            throw new ForbiddenException(logId, ApiStatus.ERR_FORBIDDEN);
        }

        if (order.getStatus() != OrderStatus.DELIVERED) {
            throw new InvalidParameterException(logId, order.getStatus(), ApiStatus.ERR_INVALID_PARAMETER);
        }

        if (reviewRepository.existsByOrderItemIdAndDeletedFalse(orderItem.getId())) {
            throw new DuplicateValueException(logId, ApiStatus.ERR_REVIEW_ALREADY_EXIST);
        }

        ProductEntity product = orderItem.getProduct();
        ReviewEntity review = ReviewEntity.builder()
                .rating(request.getRating())
                .comment(request.getComment())
                .product(product)
                .orderItem(orderItem)
                .build();

        ReviewEntity saved = reviewRepository.save(review);

        Double avg = reviewRepository.calculateAverageRatingByProductId(product.getId());
        product.setAverageRating(avg);
        productRepository.save(product);

        log.debug("Response -- logId: {}, created review id: {}", logId, saved.getId());
        return saved.map2FullDTO();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReviewDTO.Full> findAllByProductId(Long logId, Long productId, Pageable pageable) {
        log.debug("Request -- logId: {}, findAllByProductId: {}, pageable: {}", logId, productId, pageable);

        Page<ReviewDTO.Full> result = reviewRepository
                .findAllByProductIdAndDeletedFalse(productId, pageable)
                .map(ReviewEntity::map2FullDTO);

        log.debug("Response -- logId: {}, total reviews: {}", logId, result.getTotalElements());
        return result;
    }

    @Override
    @Transactional
    public void deleteById(Long logId, Long id) {
        log.debug("Request -- logId: {}, delete review id: {}", logId, id);

        ReviewEntity review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(logId, id, ApiStatus.ERR_ID_NOT_FOUND));

        Long productId = review.getProduct().getId();

        reviewRepository.deleteById(id);

        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(logId, productId, ApiStatus.ERR_ID_NOT_FOUND));
        Double avg = reviewRepository.calculateAverageRatingByProductId(productId);
        product.setAverageRating(avg);
        productRepository.save(product);

        log.debug("Response -- logId: {}, deleted review id: {}", logId, id);
    }
}
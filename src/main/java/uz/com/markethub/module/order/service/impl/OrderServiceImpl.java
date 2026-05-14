package uz.com.markethub.module.order.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.com.markethub.core.exception.InvalidParameterException;
import uz.com.markethub.core.exception.ResourceNotFoundException;
import uz.com.markethub.core.record.ApiStatus;
import uz.com.markethub.module.User.domain.UserEntity;
import uz.com.markethub.module.User.service.UserService;
import uz.com.markethub.module.cart.domain.CartEntity;
import uz.com.markethub.module.cart.domain.CartItemEntity;
import uz.com.markethub.module.cart.service.CartService;
import uz.com.markethub.module.order.constants.OrderStatus;
import uz.com.markethub.module.order.domain.OrderEntity;
import uz.com.markethub.module.order.domain.OrderItemEntity;
import uz.com.markethub.module.order.dto.CheckoutDTO;
import uz.com.markethub.module.order.dto.OrderDTO;
import uz.com.markethub.module.order.repository.OrderRepository;
import uz.com.markethub.module.order.service.OrderService;
import uz.com.markethub.module.product.domain.ProductEntity;
import uz.com.markethub.module.product.service.ProductService;
import uz.com.markethub.security.util.SecurityUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;
    private final CartService cartService;
    private final ProductService productService;
    private final UserService userService;

    @Override
    @Transactional
    public List<OrderDTO.Full> checkout(Long logId, CheckoutDTO dto) {
        log.debug("Request -- logId: {}, checkout data: {}", logId, dto);

        Long buyerId = SecurityUtils.getAuthenticatedUserId();
        CartEntity cart = cartService.findCartByBuyerIdWithItems(logId, buyerId);

        if (cart.getItems().isEmpty()) {
            throw new InvalidParameterException(logId, buyerId, ApiStatus.ERR_CART_EMPTY);
        }

        // Re-load buyer as managed entity in the current transaction
        UserEntity buyer = userService.findEntityById(logId, buyerId);

        // Group cart items by seller (product.createdBy is the seller's username)
        Map<String, List<CartItemEntity>> itemsBySeller = cart.getItems().stream()
                .collect(Collectors.groupingBy(item -> item.getProduct().getCreatedBy()));

        List<OrderEntity> createdOrders = new ArrayList<>();

        for (Map.Entry<String, List<CartItemEntity>> entry : itemsBySeller.entrySet()) {
            String sellerUsername = entry.getKey();
            List<CartItemEntity> sellerItems = entry.getValue();

            BigDecimal totalAmount = BigDecimal.ZERO;
            List<OrderItemEntity> orderItems = new ArrayList<>();

            for (CartItemEntity cartItem : sellerItems) {
                ProductEntity product = cartItem.getProduct();
                Integer requestedQty = cartItem.getQuantity();

                // Decrease stock — throws ERR_INSUFFICIENT_STOCK or ObjectOptimisticLockingFailureException
                productService.decreaseStock(logId, product, requestedQty);

                BigDecimal lineTotal = product.getPrice().multiply(BigDecimal.valueOf(requestedQty));
                totalAmount = totalAmount.add(lineTotal);

                orderItems.add(OrderItemEntity.builder()
                        .product(product)
                        // Price and name snapshots preserve historical data
                        .productNameSnapshot(product.getNameByLanguage())
                        .productSkuSnapshot(product.getSku())
                        .sellerUsername(sellerUsername)
                        .priceAtPurchase(product.getPrice())
                        .quantity(requestedQty)
                        .build());
            }

            OrderEntity order = OrderEntity.builder()
                    .buyer(buyer)
                    .shippingAddress(dto.getShippingAddress())
                    .status(OrderStatus.PENDING)
                    .totalAmount(totalAmount)
                    .build();

            // Wire item ↔ order before cascade save
            orderItems.forEach(item -> {
                item.setOrder(order);
                order.getItems().add(item);
            });

            createdOrders.add(repository.save(order));
        }

        // Clear cart only after all orders persist successfully
        cartService.clearCart(logId, cart);

        List<OrderDTO.Full> result = createdOrders.stream().map(OrderEntity::map2FullDTO).toList();
        log.debug("Response -- logId: {}, orders created: {}", logId, result.size());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO.Full> findAllMyOrders(Long logId) {
        log.debug("Request -- logId: {}, findAllMyOrders", logId);

        Long buyerId = SecurityUtils.getAuthenticatedUserId();
        List<OrderDTO.Full> result = repository.findAllByBuyerIdWithItems(buyerId)
                .stream()
                .map(OrderEntity::map2FullDTO)
                .toList();

        log.debug("Response -- logId: {}, count: {}", logId, result.size());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO.Full findMyOrderById(Long logId, Long orderId) {
        log.debug("Request -- logId: {}, findMyOrderById: {}", logId, orderId);

        Long buyerId = SecurityUtils.getAuthenticatedUserId();
        OrderDTO.Full result = repository
                .findByIdAndBuyerIdWithItems(orderId, buyerId)
                .map(OrderEntity::map2FullDTO)
                .orElseThrow(() -> new ResourceNotFoundException(logId, orderId, ApiStatus.ERR_ID_NOT_FOUND));

        log.debug("Response -- logId: {}, data: {}", logId, result);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDTO> findAllSellerOrders(Long logId, Pageable pageable) {
        log.debug("Request -- logId: {}, findAllSellerOrders pageable: {}", logId, pageable);

        String sellerUsername = SecurityUtils.getCurrentUserPrincipal().getUsername();
        Page<OrderDTO> result = repository
                .findAllBySellerUsername(sellerUsername, pageable)
                .map(OrderEntity::map2DTO);

        log.debug("Response -- logId: {}, totalElements: {}", logId, result.getTotalElements());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO.Full findSellerOrderById(Long logId, Long orderId) {
        log.debug("Request -- logId: {}, findSellerOrderById: {}", logId, orderId);

        String sellerUsername = SecurityUtils.getCurrentUserPrincipal().getUsername();
        OrderDTO.Full result = repository
                .findByIdAndSellerUsernameWithItems(orderId, sellerUsername)
                .map(OrderEntity::map2FullDTO)
                .orElseThrow(() -> new ResourceNotFoundException(logId, orderId, ApiStatus.ERR_ID_NOT_FOUND));

        log.debug("Response -- logId: {}, data: {}", logId, result);
        return result;
    }

    @Override
    @Transactional
    public OrderDTO.Full updateOrderStatus(Long logId, Long orderId, OrderDTO.UpdateStatus dto) {
        log.debug("Request -- logId: {}, updateOrderStatus orderId: {}, newStatus: {}", logId, orderId, dto.getStatus());

        String sellerUsername = SecurityUtils.getCurrentUserPrincipal().getUsername();

        // JOIN FETCH ensures seller ownership check and avoids N+1 on the response items
        OrderEntity order = repository.findByIdAndSellerUsernameWithItems(orderId, sellerUsername)
                .orElseThrow(() -> new ResourceNotFoundException(logId, orderId, ApiStatus.ERR_ID_NOT_FOUND));

        // Validate state machine transition
        if (!order.getStatus().canTransitionTo(dto.getStatus())) {
            throw new InvalidParameterException(
                    logId,
                    order.getStatus().name() + " → " + dto.getStatus().name(),
                    ApiStatus.ERR_INVALID_ORDER_TRANSITION);
        }

        order.setStatus(dto.getStatus());
        OrderDTO.Full result = repository.save(order).map2FullDTO();

        log.debug("Response -- logId: {}, data: {}", logId, result);
        return result;
    }
}
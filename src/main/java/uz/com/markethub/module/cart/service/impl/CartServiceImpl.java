package uz.com.markethub.module.cart.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.com.markethub.core.exception.InvalidParameterException;
import uz.com.markethub.core.exception.ResourceNotFoundException;
import uz.com.markethub.core.record.ApiStatus;
import uz.com.markethub.module.User.domain.UserEntity;
import uz.com.markethub.module.User.service.UserService;
import uz.com.markethub.module.cart.domain.CartEntity;
import uz.com.markethub.module.cart.domain.CartItemEntity;
import uz.com.markethub.module.cart.dto.CartDTO;
import uz.com.markethub.module.cart.repository.CartRepository;
import uz.com.markethub.module.cart.service.CartService;
import uz.com.markethub.module.product.domain.ProductEntity;
import uz.com.markethub.module.product.service.ProductService;
import uz.com.markethub.security.util.SecurityUtils;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository repository;
    private final ProductService productService;
    private final UserService userService;

    @Override
    @Transactional(readOnly = true)
    public CartDTO.Full getMyCart(Long logId) {
        log.debug("Request -- logId: {}, getMyCart", logId);

        Long buyerId = SecurityUtils.getAuthenticatedUserId();
        CartDTO.Full result = repository
                .findByBuyerIdWithItems(buyerId)
                .map(CartEntity::map2FullDTO)
                .orElse(CartDTO.Full.builder().buyerId(buyerId).items(List.of()).build());

        log.debug("Response -- logId: {}, data: {}", logId, result);
        return result;
    }

    @Override
    @Transactional
    public CartDTO.Full addItem(Long logId, CartDTO.AddOrUpdate dto) {
        log.debug("Request -- logId: {}, addItem data: {}", logId, dto);

        Long buyerId = SecurityUtils.getAuthenticatedUserId();
        CartEntity cart = getOrCreateCart(logId, buyerId);
        ProductEntity product = productService.findEntityById(logId, dto.getProductId());

        if (product.getQuantity() < dto.getQuantity()) {
            throw new InvalidParameterException(logId, product.getSku(), ApiStatus.ERR_INSUFFICIENT_STOCK);
        }

        Optional<CartItemEntity> existing = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(dto.getProductId()))
                .findFirst();

        if (existing.isPresent()) {
            existing.get().setQuantity(existing.get().getQuantity() + dto.getQuantity());
        } else {
            CartItemEntity newItem = CartItemEntity.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(dto.getQuantity())
                    .build();
            cart.getItems().add(newItem);
        }

        CartDTO.Full result = repository.save(cart).map2FullDTO();
        log.debug("Response -- logId: {}, data: {}", logId, result);
        return result;
    }

    @Override
    @Transactional
    public CartDTO.Full removeItem(Long logId, Long productId) {
        log.debug("Request -- logId: {}, removeItem productId: {}", logId, productId);

        Long buyerId = SecurityUtils.getAuthenticatedUserId();
        CartEntity cart = findCartByBuyerIdWithItems(logId, buyerId);

        boolean removed = cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
        if (!removed) {
            throw new ResourceNotFoundException(logId, productId, ApiStatus.ERR_ID_NOT_FOUND);
        }

        CartDTO.Full result = repository.save(cart).map2FullDTO();
        log.debug("Response -- logId: {}, data: {}", logId, result);
        return result;
    }

    @Override
    @Transactional
    public CartDTO.Full updateItemQuantity(Long logId, Long productId, CartDTO.AddOrUpdate dto) {
        log.debug("Request -- logId: {}, updateItemQuantity productId: {}, data: {}", logId, productId, dto);

        Long buyerId = SecurityUtils.getAuthenticatedUserId();
        CartEntity cart = findCartByBuyerIdWithItems(logId, buyerId);

        CartItemEntity item = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(logId, productId, ApiStatus.ERR_ID_NOT_FOUND));

        ProductEntity product = item.getProduct();
        if (product.getQuantity() < dto.getQuantity()) {
            throw new InvalidParameterException(logId, product.getSku(), ApiStatus.ERR_INSUFFICIENT_STOCK);
        }

        item.setQuantity(dto.getQuantity());

        CartDTO.Full result = repository.save(cart).map2FullDTO();
        log.debug("Response -- logId: {}, data: {}", logId, result);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public CartEntity findCartByBuyerIdWithItems(Long logId, Long buyerId) {
        log.debug("Request -- logId: {}, findCartByBuyerId: {}", logId, buyerId);

        return repository.findByBuyerIdWithItems(buyerId)
                .orElseThrow(() -> new ResourceNotFoundException(logId, buyerId, ApiStatus.ERR_ID_NOT_FOUND));
    }

    @Override
    @Transactional
    public void clearCart(Long logId, CartEntity cart) {
        log.debug("Request -- logId: {}, clearCart cartId: {}", logId, cart.getId());

        cart.getItems().clear();
        repository.save(cart);

        log.debug("Response -- logId: {}, cart cleared", logId);
    }

    private CartEntity getOrCreateCart(Long logId, Long buyerId) {
        return repository.findByBuyerIdWithItems(buyerId)
                .orElseGet(() -> {
                    UserEntity buyer = userService.findEntityById(logId, buyerId);
                    return repository.save(CartEntity.builder().buyer(buyer).build());
                });
    }
}
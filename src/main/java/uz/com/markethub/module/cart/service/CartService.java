package uz.com.markethub.module.cart.service;

import uz.com.markethub.module.cart.domain.CartEntity;
import uz.com.markethub.module.cart.dto.CartDTO;

public interface CartService {

    CartDTO.Full getMyCart(Long logId);

    CartDTO.Full addItem(Long logId, CartDTO.AddOrUpdate dto);

    CartDTO.Full removeItem(Long logId, Long productId);

    CartDTO.Full updateItemQuantity(Long logId, Long productId, CartDTO.AddOrUpdate dto);

    CartEntity findCartByBuyerIdWithItems(Long logId, Long buyerId);

    void clearCart(Long logId, CartEntity cart);
}
package uz.com.markethub.module.order.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.com.markethub.module.order.dto.CheckoutDTO;
import uz.com.markethub.module.order.dto.OrderDTO;

import java.util.List;

public interface OrderService {

    // Buyer operations
    List<OrderDTO.Full> checkout(Long logId, CheckoutDTO dto);

    List<OrderDTO.Full> findAllMyOrders(Long logId);

    OrderDTO.Full findMyOrderById(Long logId, Long orderId);

    // Seller operations
    Page<OrderDTO> findAllSellerOrders(Long logId, Pageable pageable);

    OrderDTO.Full findSellerOrderById(Long logId, Long orderId);

    OrderDTO.Full updateOrderStatus(Long logId, Long orderId, OrderDTO.UpdateStatus dto);
}
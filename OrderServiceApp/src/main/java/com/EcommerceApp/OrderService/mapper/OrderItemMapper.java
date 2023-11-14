package com.EcommerceApp.OrderService.mapper;

import com.EcommerceApp.OrderService.dto.OrderItemDTO;
import com.EcommerceApp.OrderService.model.OrderItem;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {
    public OrderItemDTO convertToDTO(OrderItem orderItem) {
        return new OrderItemDTO(
                orderItem.getOrderId(),
                orderItem.getProductId(),
                orderItem.getQuantity(),
                orderItem.getItemPrice()
        );
    }

    public OrderItem convertToEntity(OrderItemDTO orderItemDTO) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(orderItemDTO.getOrderId());
        orderItem.setProductId(orderItemDTO.getProductId());
        orderItem.setQuantity(orderItemDTO.getQuantity());
        orderItem.setItemPrice(orderItemDTO.getItemPrice());
        return orderItem;
    }
}

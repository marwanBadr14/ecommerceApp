package com.EcommerceApp.OrderService.mapper;

import com.EcommerceApp.OrderService.model.OrderItem;
import org.dto.OrderItemDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class OrderItemMapper {
    public OrderItemDTO convertToDTO(OrderItem orderItem) {
        return new OrderItemDTO(
                orderItem.getOrderItemId(),
                orderItem.getOrderId(),
                orderItem.getProductId(),
                orderItem.getQuantity(),
                orderItem.getItemPrice()
        );
    }

    public OrderItem convertToEntity(OrderItemDTO orderItemDTO) {
        if (orderItemDTO == null)
            return null;

        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(orderItemDTO.getOrderId());
        orderItem.setProductId(orderItemDTO.getProductId());
        orderItem.setQuantity(orderItemDTO.getQuantity());
        orderItem.setItemPrice(orderItemDTO.getItemPrice());
        return orderItem;
    }

    public List<OrderItemDTO> convertToDTO(List<OrderItem> orderItems){
        return Optional.ofNullable(orderItems)
                .map(list -> list.stream()
                        .map(orderItem -> OrderItemDTO.builder()
                                .orderId(orderItem.getOrderId())
                                .productId(orderItem.getProductId())
                                .quantity(orderItem.getQuantity())
                                .itemPrice(orderItem.getItemPrice())
                                .build())
                        .collect(Collectors.toList()))
                .orElse(null);
    }


}
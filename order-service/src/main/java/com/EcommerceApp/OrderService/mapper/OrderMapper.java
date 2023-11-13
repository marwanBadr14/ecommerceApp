package com.EcommerceApp.OrderService.mapper;

import com.EcommerceApp.OrderService.model.Order;
import com.EcommerceApp.OrderService.dto.OrderDTO;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderMapper {
    private static OrderItemMapper orderItemMapper;


    public static OrderDTO orderToOrderDTO(Order order) {
        return OrderDTO.builder()
                .orderId(order.getOrderId())
                .customerId(order.getCustomerId())
                .orderDate(order.getOrderDate())
                .totalAmount(order.getTotalAmount())
                .orderStatus(order.getOrderStatus())
                .orderItems(order.getOrderItems().stream()
                        .map(orderItemMapper::convertToDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    public static Order orderDTOToOrder(OrderDTO orderDTO) {
        Order order = new Order();
        order.setOrderId(orderDTO.getOrderId());
        order.setCustomerId(orderDTO.getCustomerId());
        order.setOrderDate(orderDTO.getOrderDate());
        order.setTotalAmount(orderDTO.getTotalAmount());
        order.setOrderStatus(orderDTO.getOrderStatus());
        order.setOrderItems(orderDTO.getOrderItems().stream()
                .map(orderItemMapper::convertToEntity)
                .collect(Collectors.toList()));
        return order;
    }
}

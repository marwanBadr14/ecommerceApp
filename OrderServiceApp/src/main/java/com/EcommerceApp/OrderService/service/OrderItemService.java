package com.EcommerceApp.OrderService.service;

import com.EcommerceApp.OrderService.dto.OrderDTO;
import com.EcommerceApp.OrderService.dto.OrderItemDTO;
import com.EcommerceApp.OrderService.dto.OrderItemPK;
import com.EcommerceApp.OrderService.dao.OrderItemDao;
import com.EcommerceApp.OrderService.exception.*;
import com.EcommerceApp.OrderService.feign.InventoryServiceClient;
import com.EcommerceApp.OrderService.mapper.OrderItemMapper;
import com.EcommerceApp.OrderService.mapper.OrderMapper;
import com.EcommerceApp.OrderService.model.OrderItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderItemService {
    // TODO: 11/14/2023 use constructor injection
    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private OrderService orderService;

    @Autowired
    InventoryServiceClient inventoryServiceClient;

    @Autowired
    private OrderMapper orderMapper;


    public OrderItemDTO createOrderItem(OrderItem orderItem) {
        OrderDTO order = orderService.findById(orderItem.getOrderId());
        orderItem.setItemPrice(inventoryServiceClient.getProductPrice(orderItem.getProductId()).getBody());
        order.setTotalAmount(order.getTotalAmount()
                .add(orderItem.getItemPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()))));
        orderService.save(orderMapper.convertToEntity(order));
        inventoryServiceClient.deductFromStock(orderItem.getProductId(),orderItem.getQuantity());
        OrderItem createdOrderItem = orderItemDao.save(orderItem);
        return orderItemMapper.convertToDTO(createdOrderItem);
    }


    public OrderItemDTO getOrderItemById(Integer orderId, Integer productId) {
        // TODO: 11/14/2023 use more meaningfully variable name
        OrderItemPK pk = new OrderItemPK();
        pk.setOrderId(orderId);
        pk.setProductId(productId);
        Optional<OrderItem> orderItem = orderItemDao.findById(pk);
        validatOrderId(orderId);
        validatProductId(productId);
        if(orderItem.isPresent()){
            return orderItemMapper.convertToDTO(orderItem.get());
        }else {
            throw new OrderItemNotFoundException("Order Item with order id: "+ orderId+ " and product id: "+ productId+"not found");
        }
    }

    public void deleteOrderItem(Integer orderId, Integer productId) {
        // TODO: 11/14/2023 use more meaningfully variable name
        OrderItemPK pk = new OrderItemPK();
        pk.setOrderId(orderId);
        pk.setProductId(productId);
        validatOrderId(orderId);
        validatProductId(productId);
        //TODO:not found
        orderItemDao.deleteById(pk);
    }


    public OrderItemDTO updateOrderItem(Integer orderId, Integer productId, OrderItem updatedOrderItem) {
        // TODO: 11/14/2023 use more meaningfully variable name
        OrderItemPK pk = new OrderItemPK();
        pk.setOrderId(orderId);
        pk.setProductId(productId);
        validatOrderId(orderId);
        validatProductId(productId);
        Optional<OrderItem> existingOrderItem = orderItemDao.findById(pk);
        if (existingOrderItem.isPresent()) {
            if(!orderId.equals(updatedOrderItem.getOrderId()))
                throw new OrderIdModificationException("Cannot Modify Order Id");
            if(!productId.equals(updatedOrderItem.getProductId()))
                throw new ProductIdModificationException("Cannot Modify Product Id");
            OrderItem orderItem = existingOrderItem.get();
            return orderItemMapper.convertToDTO(orderItemDao.save(orderItem));
        }else{
            throw new OrderItemNotFoundException("Order Item with order id: "+ orderId+ " and product id: "+ productId+"not found");
        }
    }

    public List<OrderItemDTO> findByOrderId(Integer orderId) {
        validatOrderId(orderId);
        List<OrderItem> orderItemList = orderItemDao.findByOrderId(orderId);
        return orderItemList.stream().map(orderItemMapper::convertToDTO).collect(Collectors.toList());
    }

    // TODO: 11/14/2023 move them to a validation class better design
    private void validatOrderId(Integer orderId){
        if(orderId <= 0) throw new InvalidOrderIdException("Order id is Invalid");
    }
    private void validatProductId(Integer productId){
        if(productId <= 0) throw new InvalidOrderIdException("Product id is Invalid");
    }
}
package com.EcommerceApp.OrderService.service;

import com.EcommerceApp.OrderService.dto.OrderDTO;
import com.EcommerceApp.OrderService.dto.OrderItemDTO;
import com.EcommerceApp.OrderService.exception.*;
import com.EcommerceApp.OrderService.feign.PurchaseServiceIClient;
import com.EcommerceApp.OrderService.kafka.OrderProducer;
import com.EcommerceApp.OrderService.mapper.OrderMapper;
import com.EcommerceApp.OrderService.model.Order;
import com.EcommerceApp.OrderService.Status;
import com.EcommerceApp.OrderService.dao.OrderDao;
import com.gizasystems.purchasingservice.dto.PurchaseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    OrderDao orderDao;


    private OrderMapper orderMapper;

    private PurchaseServiceIClient purchaseServiceIClient;

    private OrderProducer orderProducer;


    public OrderDTO save(Order order) {
        return orderMapper.convertToDTO(orderDao.save(order));
    }

    public List<OrderDTO> findAll() {
        return orderDao.findAll().stream().map(orderMapper::convertToDTO).collect(Collectors.toList());
    }

    public OrderDTO findById(int orderId) {
        validateOrderId(orderId);
        Optional<Order> order = orderDao.findById(orderId);
        if(order.isPresent()) {
            return orderMapper.convertToDTO(order.get());
        }else {
            throw new OrderNotFoundException("Order with order id: "+ orderId+" not found");
        }
    }

    public void deleteById(int orderId) {
        validateOrderId(orderId);
        if(existsById(orderId)) {
            orderDao.deleteById(orderId);
        }else{
            throw new OrderNotFoundException("Order with order id: "+ orderId+" not found");
        }
    }


    public List<OrderDTO> findByCustomerId(int customerId) {
        validateCustomerId(customerId);
        return orderDao.findByCustomerId(customerId).stream().map(orderMapper::convertToDTO).collect(Collectors.toList());
    }

    public List<OrderDTO> findByOrderStatus(Status orderStatus) {
        return orderDao.findByOrderStatus(orderStatus).stream().map(orderMapper::convertToDTO).collect(Collectors.toList());
    }

    public List<OrderDTO> findByTotalAmountGreaterThan(BigDecimal amount) {
        return orderDao.findByTotalAmountGreaterThan(amount).stream().map(orderMapper::convertToDTO).collect(Collectors.toList());
    }

    public List<OrderDTO> findByOrderDateBetween(Date startDate, Date endDate) {
        if(startDate.after(endDate)){
            throw new InvalidDateRangeException("Invalid date range: Start date must be on or before the end date.");
        }
        return orderDao.findByOrderDateBetween(startDate,endDate).stream().map(orderMapper::convertToDTO).collect(Collectors.toList());
    }

    public boolean existsById(int orderId) {
        return orderDao.existsById(orderId);
    }

    public List<OrderItemDTO> executeOrder(int orderId) {
        if (existsById(orderId)) {
            OrderDTO order = findById(orderId);
            List<PurchaseDTO> purchaseDTOS = new ArrayList<>();
            for (OrderItemDTO item:order.getOrderItems()) {
                purchaseDTOS.add(new PurchaseDTO(item.getProductId(), item.getQuantity()));
            }
            purchaseServiceIClient.processPurchasesRequest(purchaseDTOS);
            orderProducer.sendMessage(orderMapper.convertToEntity(order));
            return order.getOrderItems();
        } else {
            throw new OrderNotFoundException("Order with ID " + orderId + " not found");
        }

    }

    private void validateOrderId(Integer orderId){
        if(orderId <= 0) throw new InvalidOrderIdException("Order id is Invalid");
    }

    private void validateCustomerId(Integer customerId){
        if(customerId <= 0) throw new InvalidCustomerIdException("Customer id is Invalid");
    }

    @ExceptionHandler(InvalidCustomerIdException.class)
    public ResponseEntity<String> handleInvalidCustomerIdException(InvalidCustomerIdException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    public OrderDTO update(int orderId, Order updatedOrder) {
        OrderDTO orderDTO = findById(orderId);
        Order order = orderMapper.convertToEntity(orderDTO);
        if(updatedOrder.getOrderStatus()!=null)order.setOrderStatus(updatedOrder.getOrderStatus());
        if(updatedOrder.getOrderItems()!=null)order.setOrderItems(updatedOrder.getOrderItems());
        if(updatedOrder.getTotalAmount()!=null)order.setTotalAmount(updatedOrder.getTotalAmount());
       save(order);
       return orderMapper.convertToDTO(order);
    }
}
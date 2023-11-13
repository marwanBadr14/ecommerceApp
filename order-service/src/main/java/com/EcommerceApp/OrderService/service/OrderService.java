package com.EcommerceApp.OrderService.service;

import com.EcommerceApp.OrderService.model.Order;
import com.EcommerceApp.OrderService.Status;
import com.EcommerceApp.OrderService.dao.OrderDao;
import com.EcommerceApp.OrderService.model.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderService {

    @Autowired
    OrderDao orderDao;

    public Order save(Order order) {
        return orderDao.save(order);
    }

    public List<Order> findAll() {
        return orderDao.findAll();
    }

    public Optional<Order> findById(int orderId) {
        return orderDao.findById(orderId);
    }

    public void deleteById(int orderId) {
        orderDao.deleteById(orderId);
    }


    public List<Order> findByCustomerId(int customerId) {
        return orderDao.findByCustomerId(customerId);
    }

    public List<Order> findByOrderStatus(Status orderStatus) {
        return orderDao.findByOrderStatus(orderStatus);
    }

    public List<Order> findByTotalAmountGreaterThan(BigDecimal amount) {
        return orderDao.findByTotalAmountGreaterThan(amount);
    }

    public List<Order> findByOrderDateBetween(Date startDate, Date endDate) {
        return orderDao.findByOrderDateBetween(startDate,endDate);
    }

    public boolean existsById(int orderId) {
        return orderDao.existsById(orderId);
    }

    public List<OrderItem> executeOrder(int orderId) {
        Optional<Order> orderOptional = orderDao.findById(orderId);
        if (orderOptional.isPresent()) {
            return orderOptional.get().getOrderItems();
        } else {
            return new ArrayList<>();
        }
    }

}

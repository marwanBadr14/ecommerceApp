package com.EcommerceApp.OrderService.service;

import com.EcommerceApp.OrderService.model.Order;
import com.EcommerceApp.OrderService.Status;
import com.EcommerceApp.OrderService.dao.OrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
}

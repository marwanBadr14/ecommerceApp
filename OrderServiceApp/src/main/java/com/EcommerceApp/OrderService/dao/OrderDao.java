package com.EcommerceApp.OrderService.dao;

import com.EcommerceApp.OrderService.model.Order;
import com.EcommerceApp.OrderService.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderDao extends JpaRepository<Order, Integer> {
    List<Order> findByCustomerId(int customerId);

    List<Order> findByOrderStatus(Status orderStatus);

    List<Order> findByTotalAmountGreaterThan(BigDecimal amount);

   // List<Order> findByOrderDateBetween(LocalDateTime orderDate, LocalDateTime orderDate2);
}

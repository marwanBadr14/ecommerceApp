package com.EcommerceApp.OrderService.controller;

import com.EcommerceApp.OrderService.exception.OrderNotFoundException;
import com.EcommerceApp.OrderService.model.Order;
import com.EcommerceApp.OrderService.model.OrderItem;
import com.EcommerceApp.OrderService.service.OrderService;
import com.EcommerceApp.OrderService.Status;
import com.EcommerceApp.OrderService.feign.PurchaseServiceIClient;
import com.gizasystems.purchasingservice.dto.PurchaseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SuppressWarnings("deprecation")
public class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;
    @Mock
    private OrderService orderService;
    @Mock
    private PurchaseServiceIClient purchaseServiceIClient;

    private Order testOrder;
    private List<Order> testOrders;

    @BeforeEach
    public void setUp() {
        testOrder = new Order();
        testOrder.setOrderId(1);
        testOrder.setCustomer(108);
        testOrder.setOrderDate(LocalDateTime.now());
        testOrder.setTotalAmount(BigDecimal.valueOf(149.99));
        testOrder.setOrderStatus(Status.Pending);

        testOrders = new ArrayList<>();
        testOrders.add(testOrder);
        testOrders.add(new Order(105, LocalDateTime.now(), BigDecimal.valueOf(100.0), Status.Pending));
        testOrders.add(new Order(104, LocalDateTime.now(), BigDecimal.valueOf(200.0), Status.Shipped));
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateOrder() {
        when(orderService.save(any(Order.class))).thenReturn(testOrder);
        Order newOrder = new Order();
        ResponseEntity<Order> responseEntity = orderController.createOrder(newOrder);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(testOrder, responseEntity.getBody());
        verify(orderService, times(1)).save(any(Order.class));
    }

    @Test
    public void testGetAllOrders() {
        when(orderService.findAll()).thenReturn(testOrders);
        ResponseEntity<List<Order>> responseEntity = orderController.getAllOrders();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<Order> orders = responseEntity.getBody();
        assertEquals(testOrders, orders);
    }

    @Test
    public void testGetAllOrdersEmptyList() {
        when(orderService.findAll()).thenReturn(new ArrayList<>());
        ResponseEntity<List<Order>> responseEntity = orderController.getAllOrders();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<Order> orders = responseEntity.getBody();
        assertEquals(0, orders.size());
    }

    @Test
    public void testGetOrderByIdExistingOrder() {
        when(orderService.findById(1)).thenReturn(Optional.of(testOrder));
        ResponseEntity<Order> responseEntity = orderController.getOrderById(1);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(testOrder, responseEntity.getBody());
    }

    @Test
    public void testGetOrderByIdNonExistingOrder() {
        when(orderService.findById(20)).thenReturn(Optional.empty());
        OrderNotFoundException exception = assertThrows(OrderNotFoundException.class, () -> orderController.getOrderById(2));
        assertEquals("Order with ID 2 not found", exception.getMessage());
    }

    @Test
    public void testUpdateOrderExistingOrder() {
        when(orderService.existsById(1)).thenReturn(true);
        when(orderService.save(any(Order.class))).thenReturn(testOrder);
        Order updatedOrder = new Order(105, LocalDateTime.now(), BigDecimal.valueOf(700.0), Status.Processing);
        ResponseEntity<Order> responseEntity = orderController.updateOrder(1, updatedOrder);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(testOrder, responseEntity.getBody());
        verify(orderService, times(1)).save(updatedOrder);
    }

    @Test
    public void testUpdateOrderNonExistingOrder() {
        when(orderService.existsById(2)).thenReturn(false);
        Order updatedOrder = new Order(105, LocalDateTime.now(), BigDecimal.valueOf(700.0), Status.Processing);
        OrderNotFoundException exception = assertThrows(OrderNotFoundException.class, () -> orderController.updateOrder(20, updatedOrder));
        assertEquals("Order with ID 20 not found", exception.getMessage());
        verify(orderService, Mockito.never()).save(any(Order.class));
    }

    @Test
    public void testDeleteOrderExistingOrder() {
        when(orderService.existsById(1)).thenReturn(true);
        ResponseEntity<Void> responseEntity = orderController.deleteOrder(1);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(orderService, times(1)).deleteById(1);
    }

    @Test
    public void testDeleteOrderNonExistingOrder() {
        when(orderService.existsById(2)).thenReturn(false);
        OrderNotFoundException exception = assertThrows(OrderNotFoundException.class, () -> orderController.deleteOrder(2));
        assertEquals("Order with ID 2 not found", exception.getMessage());
        verify(orderService, Mockito.never()).deleteById(2);
    }

    @Test
    public void testGetOrdersByCustomerId() {
        int customerId = 123;
        List<Order> expectedOrders = new ArrayList<>();
        when(orderService.findByCustomerId(customerId)).thenReturn(expectedOrders);
        ResponseEntity<List<Order>> responseEntity = orderController.getOrdersByCustomerId(customerId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedOrders, responseEntity.getBody());
        verify(orderService, times(1)).findByCustomerId(customerId);
    }

    @Test
    public void testGetOrdersByStatus() {
        Status orderStatus = Status.Pending;
        List<Order> expectedOrders = new ArrayList<>();
        when(orderService.findByOrderStatus(orderStatus)).thenReturn(expectedOrders);
        ResponseEntity<List<Order>> responseEntity = orderController.getOrdersByStatus(orderStatus);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedOrders, responseEntity.getBody());
        verify(orderService, times(1)).findByOrderStatus(orderStatus);
    }

    @Test
    public void testGetOrdersWithTotalAmountGreaterThan() {
        BigDecimal amount = BigDecimal.valueOf(100);
        List<Order> expectedOrders = new ArrayList<>();
        when(orderService.findByTotalAmountGreaterThan(amount)).thenReturn(expectedOrders);
        ResponseEntity<List<Order>> responseEntity = orderController.getOrdersWithTotalAmountGreaterThan(amount);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedOrders, responseEntity.getBody());
        verify(orderService, times(1)).findByTotalAmountGreaterThan(amount);
    }

    @Test
    public void testGetOrdersWithTotalAmountGreaterThanNoOrders() {
        BigDecimal amount = BigDecimal.valueOf(200);
        when(orderService.findByTotalAmountGreaterThan(amount)).thenReturn(new ArrayList<>());
        ResponseEntity<List<Order>> responseEntity = orderController.getOrdersWithTotalAmountGreaterThan(amount);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(Objects.requireNonNull(responseEntity.getBody()).isEmpty());
        verify(orderService, times(1)).findByTotalAmountGreaterThan(amount);
    }

    @Test
    public void testGetOrdersWithTotalAmountGreaterThanException() {
        BigDecimal amount = BigDecimal.valueOf(100);
        when(orderService.findByTotalAmountGreaterThan(amount)).thenThrow(new RuntimeException("Test exception"));
        Exception exception = assertThrows(RuntimeException.class, () -> orderController.getOrdersWithTotalAmountGreaterThan(amount));
        assertEquals("Test exception", exception.getMessage());
        verify(orderService, times(1)).findByTotalAmountGreaterThan(amount);
    }

    @Test
    public void testGetOrdersWithinDateRange() {
        Date startDate = new Date();
        Date endDate = new Date();
        List<Order> expectedOrders = new ArrayList<>();
        when(orderService.findByOrderDateBetween(startDate, endDate)).thenReturn(expectedOrders);
        ResponseEntity<List<Order>> responseEntity = orderController.getOrdersWithinDateRange(startDate, endDate);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedOrders, responseEntity.getBody());
        verify(orderService, times(1)).findByOrderDateBetween(startDate, endDate);
    }

    @Test
    public void testGetOrdersWithinDateRangeNoOrders() {
        Date startDate = new Date();
        Date endDate = new Date();
        when(orderService.findByOrderDateBetween(startDate, endDate)).thenReturn(new ArrayList<>());
        ResponseEntity<List<Order>> responseEntity = orderController.getOrdersWithinDateRange(startDate, endDate);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(Objects.requireNonNull(responseEntity.getBody()).isEmpty());
        verify(orderService, times(1)).findByOrderDateBetween(startDate, endDate);
    }

    @Test
    public void testGetOrdersWithinDateRangeException() {
        Date startDate = new Date();
        Date endDate = new Date();
        when(orderService.findByOrderDateBetween(startDate, endDate)).thenThrow(new RuntimeException("Test exception"));
        Exception exception = assertThrows(RuntimeException.class, () -> orderController.getOrdersWithinDateRange(startDate, endDate));
        assertEquals("Test exception", exception.getMessage());
        verify(orderService, times(1)).findByOrderDateBetween(startDate, endDate);
    }

    @Test
    public void testExecuteOrder() {
        int orderId = 1;
        Order order = new Order();
        order.setOrderId(orderId);
        when(orderService.existsById(orderId)).thenReturn(true);
        when(orderService.executeOrder(orderId)).thenReturn(createTestOrderItems());
        List<OrderItem> orderItems = createTestOrderItems();
        List<PurchaseDTO> purchaseDTOS = createPurchaseDTOList(orderItems);
        ResponseEntity<List<OrderItem>> responseEntity = orderController.executeOrder(orderId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(orderItems, responseEntity.getBody());
        verify(orderService, times(1)).executeOrder(orderId);
        verify(purchaseServiceIClient, times(1)).processPurchasesRequest(purchaseDTOS);
    }

    @Test
    public void testExecuteOrderOrderNotFound() {
        int orderId = 1;
        when(orderService.existsById(orderId)).thenReturn(false);
        Exception exception = assertThrows(OrderNotFoundException.class, () -> orderController.executeOrder(orderId));
        assertEquals("Order with ID 1 not found", exception.getMessage());
        verify(orderService, times(1)).existsById(orderId);
        verify(purchaseServiceIClient, never()).processPurchasesRequest(anyList());
    }

    private List<OrderItem> createTestOrderItems() {
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(new OrderItem());
        return orderItems;
    }

    private List<PurchaseDTO> createPurchaseDTOList(List<OrderItem> orderItems) {
        List<PurchaseDTO> purchaseDTOS = new ArrayList<>();
        for (OrderItem item : orderItems) {
            purchaseDTOS.add(new PurchaseDTO(item.getProductId(), item.getQuantity()));
        }
        return purchaseDTOS;
    }
}

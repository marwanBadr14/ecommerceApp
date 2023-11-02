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
        // Add test orders to the list with required attributes
        testOrders.add(new Order(105, LocalDateTime.now(), BigDecimal.valueOf(100.0), Status.Pending));
        testOrders.add(new Order(104, LocalDateTime.now(), BigDecimal.valueOf(200.0), Status.Shipped));
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateOrder() {
        // Set up mock behavior for orderService
        when(orderService.save(any(Order.class))).thenReturn(testOrder);

        // Create a test order
        Order newOrder = new Order();

        ResponseEntity<Order> responseEntity = orderController.createOrder(newOrder);

        // Verify the response
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(testOrder, responseEntity.getBody());

        // Verify that the orderService's save method was called with the newOrder
        verify(orderService, times(1)).save(any(Order.class));
    }

    @Test
    public void testGetAllOrders() {
        // Set up mock behavior for orderService
        when(orderService.findAll()).thenReturn(testOrders);

        // Call the controller method
        ResponseEntity<List<Order>> responseEntity = orderController.getAllOrders();

        // Verify the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<Order> orders = responseEntity.getBody();
        assertEquals(testOrders, orders);
    }

    @Test
    public void testGetAllOrdersEmptyList() {
        // Set up mock behavior for orderService with an empty list
        when(orderService.findAll()).thenReturn(new ArrayList<>());

        // Call the controller method
        ResponseEntity<List<Order>> responseEntity = orderController.getAllOrders();

        // Verify the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<Order> orders = responseEntity.getBody();
        assertEquals(0, orders.size());
    }

    @Test
    public void testGetOrderByIdExistingOrder() {
        // Set up mock behavior for orderService
        when(orderService.findById(1)).thenReturn(Optional.of(testOrder));

        // Call the controller method with an existing orderId (1)
        ResponseEntity<Order> responseEntity = orderController.getOrderById(1);

        // Verify the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(testOrder, responseEntity.getBody());
    }

    @Test
    public void testGetOrderByIdNonExistingOrder() {
        // Set up mock behavior for orderService with an empty result
        when(orderService.findById(20)).thenReturn(Optional.empty());

        // Call the controller method with a non-existing orderId (2)
        OrderNotFoundException exception = assertThrows(OrderNotFoundException.class, () -> orderController.getOrderById(2));

        // Verify that an OrderNotFoundException is thrown with the expected message
        assertEquals("Order with ID 2 not found", exception.getMessage());
    }

    @Test
    public void testUpdateOrderExistingOrder() {
        // Set up mock behavior for orderService
        when(orderService.existsById(1)).thenReturn(true);
        when(orderService.save(any(Order.class))).thenReturn(testOrder);

        // Create an updated order
        Order updatedOrder = new Order(105, LocalDateTime.now(), BigDecimal.valueOf(700.0), Status.Processing);

        // Call the controller method with an existing orderId (1)
        ResponseEntity<Order> responseEntity = orderController.updateOrder(1, updatedOrder);

        // Verify the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(testOrder, responseEntity.getBody());

        // Verify that the orderService's save method was called with the updated order
        verify(orderService, times(1)).save(updatedOrder);
    }

    @Test
    public void testUpdateOrderNonExistingOrder() {
        // Set up mock behavior for orderService with a non-existing order
        when(orderService.existsById(2)).thenReturn(false);

        // Create an updated order
        Order updatedOrder = new Order(105, LocalDateTime.now(), BigDecimal.valueOf(700.0), Status.Processing);


        // Call the controller method with a non-existing orderId (2)
        OrderNotFoundException exception = assertThrows(OrderNotFoundException.class, () -> orderController.updateOrder(20, updatedOrder));

        // Verify that an OrderNotFoundException is thrown with the expected message
        assertEquals("Order with ID 20 not found", exception.getMessage());

        // Verify that the orderService's save method was not called
        verify(orderService, Mockito.never()).save(any(Order.class));
    }

    @Test
    public void testDeleteOrderExistingOrder() {
        // Set up mock behavior for orderService
        when(orderService.existsById(1)).thenReturn(true);

        // Call the controller method with an existing orderId (1)
        ResponseEntity<Void> responseEntity = orderController.deleteOrder(1);

        // Verify the response
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());

        // Verify that the orderService's deleteById method was called with the orderId
        verify(orderService, times(1)).deleteById(1);
    }

    @Test
    public void testDeleteOrderNonExistingOrder() {
        // Set up mock behavior for orderService with a non-existing order
        when(orderService.existsById(2)).thenReturn(false);

        // Call the controller method with a non-existing orderId (2)
        OrderNotFoundException exception = assertThrows(OrderNotFoundException.class, () -> orderController.deleteOrder(2));

        // Verify that an OrderNotFoundException is thrown with the expected message
        assertEquals("Order with ID 2 not found", exception.getMessage());

        // Verify that the orderService's deleteById method was not called
        verify(orderService, Mockito.never()).deleteById(2);
    }

    @Test
    public void testGetOrdersByCustomerId() {
        // Set up mock behavior for orderService
        int customerId = 123; // Replace with a valid customer ID
        List<Order> expectedOrders = new ArrayList<>(); // Create a list of expected orders
        when(orderService.findByCustomerId(customerId)).thenReturn(expectedOrders);

        // Call the controller method with a customer ID
        ResponseEntity<List<Order>> responseEntity = orderController.getOrdersByCustomerId(customerId);

        // Verify the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedOrders, responseEntity.getBody());

        // Verify that the orderService's findByCustomerId method was called with the customer ID
        verify(orderService, times(1)).findByCustomerId(customerId);
    }

    @Test
    public void testGetOrdersByStatus() {
        // Set up mock behavior for orderService
        Status orderStatus = Status.Pending; // Replace with a valid order status
        List<Order> expectedOrders = new ArrayList<>(); // Create a list of expected orders
        when(orderService.findByOrderStatus(orderStatus)).thenReturn(expectedOrders);

        // Call the controller method with an order status
        ResponseEntity<List<Order>> responseEntity = orderController.getOrdersByStatus(orderStatus);

        // Verify the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedOrders, responseEntity.getBody());

        // Verify that the orderService's findByOrderStatus method was called with the order status
        verify(orderService, times(1)).findByOrderStatus(orderStatus);
    }

    @Test
    public void testGetOrdersWithTotalAmountGreaterThan() {
        // Set up mock behavior for orderService
        BigDecimal amount = BigDecimal.valueOf(100); // Replace with the desired amount
        List<Order> expectedOrders = new ArrayList<>(); // Create a list of expected orders
        when(orderService.findByTotalAmountGreaterThan(amount)).thenReturn(expectedOrders);

        // Call the controller method with the specified amount
        ResponseEntity<List<Order>> responseEntity = orderController.getOrdersWithTotalAmountGreaterThan(amount);

        // Verify the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedOrders, responseEntity.getBody());

        // Verify that the orderService's findByTotalAmountGreaterThan method was called with the amount
        verify(orderService, times(1)).findByTotalAmountGreaterThan(amount);
    }

    @Test
    public void testGetOrdersWithTotalAmountGreaterThanNoOrders() {
        // Set up mock behavior for orderService with no orders for the specified amount
        BigDecimal amount = BigDecimal.valueOf(200); // Replace with the desired amount
        when(orderService.findByTotalAmountGreaterThan(amount)).thenReturn(new ArrayList<>());

        // Call the controller method with the specified amount
        ResponseEntity<List<Order>> responseEntity = orderController.getOrdersWithTotalAmountGreaterThan(amount);

        // Verify the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(Objects.requireNonNull(responseEntity.getBody()).isEmpty());

        // Verify that the orderService's findByTotalAmountGreaterThan method was called with the amount
        verify(orderService, times(1)).findByTotalAmountGreaterThan(amount);
    }

    @Test
    public void testGetOrdersWithTotalAmountGreaterThanException() {
        // Set up mock behavior for orderService to throw an exception
        BigDecimal amount = BigDecimal.valueOf(100); // Replace with the desired amount
        when(orderService.findByTotalAmountGreaterThan(amount)).thenThrow(new RuntimeException("Test exception"));

        // Call the controller method with the specified amount
        Exception exception = assertThrows(RuntimeException.class, () -> orderController.getOrdersWithTotalAmountGreaterThan(amount));

        // Verify that the expected exception is thrown
        assertEquals("Test exception", exception.getMessage());

        // Verify that the orderService's findByTotalAmountGreaterThan method was called with the amount
        verify(orderService, times(1)).findByTotalAmountGreaterThan(amount);
    }

    @Test
    public void testGetOrdersWithinDateRange() {
        // Set up mock behavior for orderService
        Date startDate = new Date(); // Replace with the desired start date
        Date endDate = new Date(); // Replace with the desired end date
        List<Order> expectedOrders = new ArrayList<>(); // Create a list of expected orders
        when(orderService.findByOrderDateBetween(startDate, endDate)).thenReturn(expectedOrders);

        // Call the controller method with the specified start and end dates
        ResponseEntity<List<Order>> responseEntity = orderController.getOrdersWithinDateRange(startDate, endDate);

        // Verify the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedOrders, responseEntity.getBody());

        // Verify that the orderService's findByOrderDateBetween method was called with the start and end dates
        verify(orderService, times(1)).findByOrderDateBetween(startDate, endDate);
    }

    @Test
    public void testGetOrdersWithinDateRangeNoOrders() {
        // Set up mock behavior for orderService with no orders for the specified date range
        Date startDate = new Date(); // Replace with the desired start date
        Date endDate = new Date(); // Replace with the desired end date
        when(orderService.findByOrderDateBetween(startDate, endDate)).thenReturn(new ArrayList<>());

        // Call the controller method with the specified start and end dates
        ResponseEntity<List<Order>> responseEntity = orderController.getOrdersWithinDateRange(startDate, endDate);

        // Verify the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(Objects.requireNonNull(responseEntity.getBody()).isEmpty());

        // Verify that the orderService's findByOrderDateBetween method was called with the start and end dates
        verify(orderService, times(1)).findByOrderDateBetween(startDate, endDate);
    }

    @Test
    public void testGetOrdersWithinDateRangeException() {
        // Set up mock behavior for orderService to throw an exception
        Date startDate = new Date(); // Replace with the desired start date
        Date endDate = new Date(); // Replace with the desired end date
        when(orderService.findByOrderDateBetween(startDate, endDate)).thenThrow(new RuntimeException("Test exception"));

        // Call the controller method with the specified start and end dates
        Exception exception = assertThrows(RuntimeException.class, () -> orderController.getOrdersWithinDateRange(startDate, endDate));

        // Verify that the expected exception is thrown
        assertEquals("Test exception", exception.getMessage());

        // Verify that the orderService's findByOrderDateBetween method was called with the start and end dates
        verify(orderService, times(1)).findByOrderDateBetween(startDate, endDate);
    }

    @Test
    public void testExecuteOrder() {
        // Set up mock behavior for orderService
        int orderId = 1;
        Order order = new Order();
        order.setOrderId(orderId);
        when(orderService.existsById(orderId)).thenReturn(true);
        when(orderService.executeOrder(orderId)).thenReturn(createTestOrderItems()); // Replace with a list of order items

        // Set up mock behavior for purchaseServiceIClient
        List<OrderItem> orderItems = createTestOrderItems(); // Replace with a list of order items
        List<PurchaseDTO> purchaseDTOS = createPurchaseDTOList(orderItems);

        // Call the controller method
        ResponseEntity<List<OrderItem>> responseEntity = orderController.executeOrder(orderId);

        // Verify the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(orderItems, responseEntity.getBody());

        // Verify that the orderService's executeOrder method was called
        verify(orderService, times(1)).executeOrder(orderId);

        // Verify that the purchaseServiceIClient's processPurchasesRequest method was called with the expected purchaseDTOS
        verify(purchaseServiceIClient, times(1)).processPurchasesRequest(purchaseDTOS);
    }

    @Test
    public void testExecuteOrderOrderNotFound() {
        // Set up mock behavior for orderService when the order is not found
        int orderId = 1;
        when(orderService.existsById(orderId)).thenReturn(false);

        // Call the controller method
        Exception exception = assertThrows(OrderNotFoundException.class, () -> orderController.executeOrder(orderId));

        // Verify that the expected OrderNotFoundException is thrown
        assertEquals("Order with ID 1 not found", exception.getMessage());

        // Verify that the orderService's existsById method was called
        verify(orderService, times(1)).existsById(orderId);

        // Verify that the purchaseServiceIClient's processPurchasesRequest method was not called
        verify(purchaseServiceIClient, never()).processPurchasesRequest(anyList());
    }

    private List<OrderItem> createTestOrderItems() {
        // Replace with logic to create a list of test OrderItem objects
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(new OrderItem());
        // Add more OrderItem objects as needed
        return orderItems;
    }

    private List<PurchaseDTO> createPurchaseDTOList(List<OrderItem> orderItems) {
        // Replace with logic to create a list of PurchaseDTO objects based on order items
        List<PurchaseDTO> purchaseDTOS = new ArrayList<>();
        for (OrderItem item : orderItems) {
            purchaseDTOS.add(new PurchaseDTO(item.getProductId(), item.getQuantity()));
        }
        return purchaseDTOS;
    }
}

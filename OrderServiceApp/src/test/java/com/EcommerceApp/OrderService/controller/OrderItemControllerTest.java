package com.EcommerceApp.OrderService.controller;

import com.EcommerceApp.OrderService.exception.InvalidOrderIdException;
import com.EcommerceApp.OrderService.exception.InvalidProductIdException;
import com.EcommerceApp.OrderService.exception.OrderItemNotFoundException;
import com.EcommerceApp.OrderService.exception.OrderNotFoundException;
import com.EcommerceApp.OrderService.feign.InventoryServiceClient;
import com.EcommerceApp.OrderService.model.Order;
import com.EcommerceApp.OrderService.model.OrderItem;
import com.EcommerceApp.OrderService.service.OrderItemService;
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
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OrderItemControllerTest {

    @InjectMocks
    private OrderItemController orderItemController;
    @Mock
    private OrderItemService orderItemService;
    @Mock
    private OrderService orderService;
    @Mock
    private InventoryServiceClient inventoryServiceClient;


    private Order validOrder;
    private OrderItem validOrderItem;
    private List<OrderItem> orderItems;
    private ResponseEntity<BigDecimal> validProductPrice;
    private ResponseEntity<Integer> validDeductionResponse;
    @BeforeEach
    public void setUp() {
        // Create a valid Order
        validOrder = new Order();
        validOrder.setOrderId(1);  // Set the ID of the order
        validOrder.setTotalAmount(new BigDecimal("100.00"));  // Set the total amount

        // Create a valid OrderItem
        validOrderItem = new OrderItem();
        validOrderItem.setOrderId(1);  // Set the order ID for the order item
        validOrderItem.setProductId(101);  // Set the product ID
        validOrderItem.setQuantity(2);  // Set the quantity
        validOrderItem.setItemPrice(new BigDecimal("50.00"));  // Set the item price
        BigDecimal productPriceValue = new BigDecimal("25.00");
        validProductPrice = new ResponseEntity<>(productPriceValue, HttpStatus.OK);
        validDeductionResponse = new ResponseEntity<>(HttpStatus.NO_CONTENT);

        orderItems = new ArrayList<>();

        // Create OrderItem objects and add them to the list
        OrderItem orderItem1 = new OrderItem();
        orderItem1.setOrderId(1);
        orderItem1.setProductId(101);
        orderItem1.setQuantity(2);
        orderItem1.setItemPrice(new BigDecimal("50.00"));
        orderItems.add(orderItem1);

        OrderItem orderItem2 = new OrderItem();
        orderItem2.setOrderId(1);
        orderItem2.setProductId(102);
        orderItem2.setQuantity(3);
        orderItem2.setItemPrice(new BigDecimal("30.00"));
        orderItems.add(orderItem2);
        MockitoAnnotations.initMocks(this);

    }

    @Test
    void testCreateOrderItem_Success() {
        when(orderService.findById(anyInt())).thenReturn(Optional.of(validOrder));
        when(inventoryServiceClient.getProductPrice(anyInt())).thenReturn(validProductPrice);
        when(inventoryServiceClient.deductFromStock(anyInt(), anyInt())).thenReturn(validDeductionResponse);
        when(orderItemService.createOrderItem(any(OrderItem.class))).thenReturn(validOrderItem);
        ResponseEntity<OrderItem> responseEntity = orderItemController.createOrderItem(validOrderItem);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(validOrderItem, responseEntity.getBody());
    }

    @Test
    public void testCreateOrderItemOrderNotFound() {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(1);
        orderItem.setProductId(101);
        orderItem.setQuantity(2);
        when(orderService.findById(1)).thenReturn(Optional.empty());
        ResponseEntity<OrderItem> responseEntity = orderItemController.createOrderItem(orderItem);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testCreateOrderItemException() {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(1);
        orderItem.setProductId(101);
        orderItem.setQuantity(2);
        Order order = new Order();
        order.setOrderId(1);
        when(orderService.findById(1)).thenReturn(Optional.of(order));
        when(inventoryServiceClient.getProductPrice(101)).thenReturn(new ResponseEntity<>(BigDecimal.valueOf(50.0), HttpStatus.OK));
        when(orderItemService.createOrderItem(any(OrderItem.class))).thenThrow(new RuntimeException());
        ResponseEntity<OrderItem> responseEntity = orderItemController.createOrderItem(orderItem);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void testGetOrderItems() {
        int orderId = 1;
        when(orderItemService.findByOrderId(orderId)).thenReturn(orderItems);
        ResponseEntity<List<OrderItem> > responseEntity = orderItemController.getOrderItems(orderId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(orderItems, responseEntity.getBody());
    }

    @Test
    public void testGetOrderItem() {
        int orderId = 1;
        int productId = 101;
        OrderItem orderItem = new OrderItem();
        when(orderItemService.getOrderItemById(orderId, productId)).thenReturn(orderItem);

        ResponseEntity<OrderItem> responseEntity = orderItemController.getOrderItem(orderId, productId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(orderItem, responseEntity.getBody());
    }

    @Test
    public void testGetOrderItemNotFound() {
        int orderId = 1;
        int productId = 101;
        when(orderItemService.getOrderItemById(orderId, productId)).thenReturn(null);
        OrderItemNotFoundException exception = assertThrows(OrderItemNotFoundException.class,
                () -> orderItemController.getOrderItem(orderId, productId));
        assertEquals("Order item with Order ID 1 and Product ID 101 not found", exception.getMessage());
    }

    @Test
    public void testGetOrderItems_NegativeOrderId() {
        int orderId = -1;
        InvalidOrderIdException exception = assertThrows(InvalidOrderIdException.class,
                () -> orderItemController.getOrderItems(orderId));
        assertEquals("Invalid order Id: -1", exception.getMessage());
    }

    @Test
    public void testGetOrderItems_Exception() {
        when(orderItemService.findByOrderId(anyInt())).thenThrow(new RuntimeException("Test exception"));
        Exception exception = assertThrows(RuntimeException.class, () -> orderItemController.getOrderItems(3));
        assertEquals("Test exception", exception.getMessage());
    }

    @Test
    public void testGetOrderItemFound() {
        int orderId = 1;
        int productId = 101;
        OrderItem expectedOrderItem = new OrderItem();
        when(orderItemService.getOrderItemById(orderId, productId)).thenReturn(expectedOrderItem);
        ResponseEntity<OrderItem> responseEntity = orderItemController.getOrderItem(orderId, productId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedOrderItem, responseEntity.getBody());
    }

    @Test
    public void testGetOrderItemNegativeOrderId() {
        int orderId = -1;
        int productId = 100;
        InvalidOrderIdException exception = assertThrows(InvalidOrderIdException.class,
                () -> orderItemController.getOrderItem(orderId, productId));
        assertEquals("Invalid order Id: -1", exception.getMessage());
    }

    @Test
    public void testGetOrderItemNegativeProductId() {
        int orderId = 1;
        int productId = -100;
        InvalidProductIdException exception = assertThrows(InvalidProductIdException.class,
                () -> orderItemController.getOrderItem(orderId, productId));
        assertEquals("Invalid product Id: -100", exception.getMessage());
    }

    @Test
    void testGetOrderItem_OrderIdNotFound() {
        int orderId = 999;
        int productId = 100;
        Mockito.when(orderItemService.getOrderItemById(orderId, productId)).thenReturn(null);
        OrderItemNotFoundException exception = assertThrows(OrderItemNotFoundException.class,
                () -> orderItemController.getOrderItem(orderId, productId));
        assertEquals("Order item with Order ID 999 and Product ID 100 not found", exception.getMessage());
    }


    @Test
    public void testUpdateOrderItem() {
        int orderId = 1;
        int productId = 101;
        OrderItem orderItem = new OrderItem();

        when(orderItemService.updateOrderItem(orderId, productId, orderItem)).thenReturn(orderItem);

        ResponseEntity<OrderItem> responseEntity = orderItemController.updateOrderItem(orderId, productId, orderItem);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(orderItem, responseEntity.getBody());
    }

    @Test
    public void testUpdateOrderItemException() {
        int orderId = 1;
        int productId = 101;
        OrderItem orderItem = new OrderItem();

        when(orderItemService.updateOrderItem(orderId, productId, orderItem)).thenThrow(new RuntimeException());

        ResponseEntity<OrderItem> responseEntity = orderItemController.updateOrderItem(orderId, productId, orderItem);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void testDeleteOrderItem() {
        int orderId = 1;
        int productId = 101;

        ResponseEntity<String> responseEntity = orderItemController.deleteOrderItem(orderId, productId);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    public void testDeleteOrderItemNotFound() {
        int orderId = 1;
        int productId = 101;

        doThrow(new OrderItemNotFoundException("Order item not found")).when(orderItemService).deleteOrderItem(orderId, productId);

        ResponseEntity<String> responseEntity = orderItemController.deleteOrderItem(orderId, productId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testDeleteOrderItemException() {
        int orderId = 1;
        int productId = 101;

        doThrow(new RuntimeException()).when(orderItemService).deleteOrderItem(orderId, productId);

        ResponseEntity<String> responseEntity = orderItemController.deleteOrderItem(orderId, productId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }
}

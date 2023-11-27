package com.gizasystems.notificationservice.kafka;

import com.EcommerceApp.OrderService.model.Order;
import com.EcommerceApp.OrderService.model.OrderItem;
import com.gizasystems.notificationservice.feign.InventoryServiceInterface;
import com.gizasystems.notificationservice.feign.UserServiceInterface;
import com.gizasystems.notificationservice.service.EmailSenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class OrderConsumer {

    @Autowired
    private EmailSenderService service;
    @Autowired
    private UserServiceInterface userServiceInterface;
    @Autowired
    private InventoryServiceInterface inventoryServiceInterface;
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderConsumer.class);

    // Email Body
    public String createEmail(Order order){
        StringBuilder body = new StringBuilder("Your Order's details \n" +
                "Order Id #" + order.getOrderId() + ": \n" +
                "Products count: " + order.getOrderItems().size() + "\n\n");
        List<OrderItem> orderItems = order.getOrderItems();
        int index = 1;
        for (OrderItem orderItem: orderItems){
            body.append("Item #").append(index).append("\n");
            body.append("product's name: ").append(inventoryServiceInterface.getProductName(orderItem.getProductId())).append("\n");
            body.append("cost: ").append(inventoryServiceInterface.getProductPrice(orderItem.getProductId())).append("\n");
            body.append("quantity: ").append(orderItem.getQuantity()).append("\n\n");
            ++index;
        }
        body.append("Total Amount: ").append(order.getTotalAmount()).append("\n");
        return body.toString();
    }

    @KafkaListener(topics = "${spring.kafka.topic.name}",
            groupId = "${spring.kafka.consumer.group-id}")
    public void consume(Order order){
        LOGGER.info(String.format("Order is received => %s", order.toString()));
        service.sendEmail(userServiceInterface.getUserEmailById(order.getCustomerId()).getBody(), createEmail(order));
    }
}
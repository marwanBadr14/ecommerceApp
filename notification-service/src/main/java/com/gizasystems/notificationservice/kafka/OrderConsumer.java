package com.gizasystems.notificationservice.kafka;

import com.EcommerceApp.OrderService.model.Order;
import com.EcommerceApp.OrderService.model.OrderItem;
import com.gizasystems.notificationservice.feign.InventoryServiceInterface;
import com.gizasystems.notificationservice.feign.UserServiceInterface;
import com.gizasystems.notificationservice.service.EmailSenderService;
import org.dto.OrderDTO;
import org.dto.OrderItemDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class OrderConsumer {

    private final EmailSenderService service;
    private final UserServiceInterface userServiceInterface;
    private final InventoryServiceInterface inventoryServiceInterface;

    public OrderConsumer(EmailSenderService service, UserServiceInterface userServiceInterface, InventoryServiceInterface inventoryServiceInterface) {
        this.service = service;
        this.userServiceInterface = userServiceInterface;
        this.inventoryServiceInterface = inventoryServiceInterface;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderConsumer.class);

    // Email Body
    public String createEmail(OrderDTO orderDTO){
        StringBuilder body = new StringBuilder("Your Order's details \n" +
                "Order Id #" + orderDTO.getOrderId() + ": \n" +
                "Products count: " + orderDTO.getOrderItems().size() + "\n\n");
        List<OrderItemDTO> orderItemsDtos = orderDTO.getOrderItems();
        int index = 1;
        for (OrderItemDTO orderItemDTO: orderItemsDtos){
            body.append("Item #").append(index).append("\n");
            body.append("product's name: ").append(inventoryServiceInterface.getProductName(orderItemDTO.getProductId()).getBody()).append("\n");
            body.append("cost: ").append(inventoryServiceInterface.getProductPrice(orderItemDTO.getProductId()).getBody()).append("\n");
            body.append("quantity: ").append(orderItemDTO.getQuantity()).append("\n\n");
            ++index;
        }
        body.append("Total Amount: ").append(orderDTO.getTotalAmount()).append("\n");
        return body.toString();
    }

    @KafkaListener(topics = "${spring.kafka.topic.name}",
            groupId = "${spring.kafka.consumer.group-id}")
    public void consume(OrderDTO orderDTO){
        LOGGER.info(String.format("Order is received => %s", orderDTO.toString()));
        service.sendEmail(userServiceInterface.getUserEmailById(orderDTO.getCustomerId()).getBody(), createEmail(orderDTO));
    }
}
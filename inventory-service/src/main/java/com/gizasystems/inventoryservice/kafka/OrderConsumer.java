package com.gizasystems.inventoryservice.kafka;


import com.EcommerceApp.OrderService.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;



@Service
public class OrderConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderConsumer.class);

    @KafkaListener(topics = "${spring.kafka.topic.name}",
    groupId = "${spring.kafka.consumer.group-id}")
    public void consume(Order event){
        LOGGER.info(String.format("Order is received in Inventory Service => %s", event.toString()));
    }
}

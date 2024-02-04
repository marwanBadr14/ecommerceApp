package com.EcommerceApp.OrderService.kafka;

import com.EcommerceApp.OrderService.feign.InventoryServiceClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.dto.OrderDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;


@Service
public class OrderProducer {

    @Autowired
    InventoryServiceClient inventoryServiceClient;
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderProducer.class);

    private final NewTopic topic;
    private final KafkaTemplate<String, OrderDTO> kafkaTemplate;


    public OrderProducer(NewTopic topic, KafkaTemplate<String, OrderDTO> kafkaTemplate) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(OrderDTO orderDTO){
        LOGGER.info(String.format("Order event => %s", orderDTO.toString()));

        // create message
        Message<OrderDTO> message = MessageBuilder
                .withPayload(orderDTO)
                .setHeader(KafkaHeaders.TOPIC,topic.name())
                .build();
        kafkaTemplate.send(message);
    }
}

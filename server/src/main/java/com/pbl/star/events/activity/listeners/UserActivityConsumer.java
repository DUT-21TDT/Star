package com.pbl.star.events.activity.listeners;

import com.pbl.star.configurations.RabbitMQConfig;
import com.pbl.star.services.external.impl.NotificationProducerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class UserActivityConsumer {
    // Map of routing keys to handlers
    private final Map<String, UserActivityHandler> handlers;
    private static final Logger logger = LoggerFactory.getLogger(NotificationProducerImpl.class);


    public UserActivityConsumer(@Autowired List<UserActivityHandler> handlers) {
        this.handlers = handlers.stream()
                .flatMap(handler -> Stream.of(handler.getRoutingKey())
                        .map(routingKey -> Map.entry(routingKey, handler)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    // Delegate the handling of the message to the appropriate handler
    @RabbitListener(queues = RabbitMQConfig.USER_ACTIVITY_QUEUE)
    public void handleUserActivity(Message message) {

        boolean hasRetried = message.getMessageProperties().getRedelivered();

        if (hasRetried) {
            // Retry limit reached, send to DLQ
            throw new AmqpRejectAndDontRequeueException("Retry limit reached for message: " + message);
        }

        String routingKey = message.getMessageProperties().getReceivedRoutingKey();

        UserActivityHandler handler = handlers.get(routingKey);
        if (handler != null) {
            try {
                if (routingKey.startsWith("notification.UNDO_")) {
                    handler.undo(message);
                } else {
                    handler.handle(message);
                }
            } catch(IOException e){
                logger.error("Failed to handle user activity message", e);
            }
        } else {
            throw new IllegalArgumentException("No handler found for routing key: " + routingKey);
        }
    }
}

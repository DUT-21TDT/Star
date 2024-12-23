package com.pbl.star.events;

import com.pbl.star.configurations.RabbitMQConfig;
import com.pbl.star.services.external.impl.NotificationProducerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class DeadLetterQueueConsumer {
    private static final Logger logger = LoggerFactory.getLogger(NotificationProducerImpl.class);

    @RabbitListener(queues = RabbitMQConfig.DLQ)
    public void handleDeadLetter(Message message) {
        logger.error("Message in DLQ: " + message.toString());
    }
}

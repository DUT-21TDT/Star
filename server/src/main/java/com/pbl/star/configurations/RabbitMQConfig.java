package com.pbl.star.configurations;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {
    public static final String NOTIFICATION_EXCHANGE = "notification_exchange";
    public static final String USER_ACTIVITY_QUEUE = "user_activity_queue";

    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(NOTIFICATION_EXCHANGE);
    }

    @Bean
    public Queue userActivityQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-message-ttl", 60 * 60 * 1000); // 1 hour
        return new Queue(USER_ACTIVITY_QUEUE, true, false, false, args); // Durable queue
    }

    @Bean
    public Binding userNotificationBinding(Queue userActivityQueue, TopicExchange notificationExchange) {
        return BindingBuilder.bind(userActivityQueue)
                .to(notificationExchange)
                .with("notification.#");
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter converter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter);
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.addTrustedPackages("com.pbl.star.events.activity");
        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }
}

package com.pbl.star.events.activity.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbl.star.configurations.JacksonConfig;
import com.pbl.star.enums.NotificationType;
import com.pbl.star.events.activity.ModeratePostEvent;
import com.pbl.star.services.domain.NotificationService;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

@Component
public class ApprovePostHandler implements UserActivityHandler {

    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;

    public ApprovePostHandler(NotificationService notificationService) {
        this.objectMapper = new JacksonConfig().queueObjectMapper();
        this.notificationService = notificationService;
    }

    @Override
    public String[] getRoutingKey() {
        return new String[]{"notification.approve_post"};
    }

    @Override
    public void handle(Message message) throws IOException {
        ModeratePostEvent event = objectMapper.readValue(message.getBody(), ModeratePostEvent.class);
        String postId = event.getPostId();
        Instant timestamp = event.getTimestamp();

        notificationService.createModeratePostNotification(postId, timestamp, NotificationType.APPROVE_POST);
    }

    @Override
    public void undo(Message message) throws IOException {

    }
}

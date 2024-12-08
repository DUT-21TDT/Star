package com.pbl.star.events.activity.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbl.star.configurations.JacksonConfig;
import com.pbl.star.enums.NotificationType;
import com.pbl.star.events.activity.FollowUserEvent;
import com.pbl.star.services.domain.NotificationService;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

@Component
public class AcceptFollowHandler implements UserActivityHandler {

    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;

    public AcceptFollowHandler(NotificationService notificationService) {
        this.objectMapper = new JacksonConfig().queueObjectMapper();
        this.notificationService = notificationService;
    }

    @Override
    public String[] getRoutingKey() {
        return new String[]{"notification.accept_follow"};
    }

    @Override
    public void handle(Message message) throws IOException {
        FollowUserEvent event = objectMapper.readValue(message.getBody(), FollowUserEvent.class);

        String followingId = event.getFollowingId();
        String followerId = event.getFollowerId();
        String followeeId = event.getFolloweeId();
        Instant timestamp = event.getTimestamp();

        notificationService.createFollowUserNotification(followingId, followerId, followeeId, timestamp, NotificationType.ACCEPT_FOLLOW);
    }

    @Override
    public void undo(Message message) throws IOException {

    }
}

package com.pbl.star.events.notification.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbl.star.configurations.JacksonConfig;
import com.pbl.star.events.notification.FollowUserEvent;
import com.pbl.star.services.domain.NotificationService;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

@Component
public class FollowHandler implements UserActivityHandler {

    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;

    public FollowHandler(NotificationService notificationService) {
        this.objectMapper = new JacksonConfig().queueObjectMapper();
        this.notificationService = notificationService;
    }

    @Override
    public String[] getRoutingKey() {
        return new String[]{"notification.follow", "notification.UNDO_follow"};
    }

    @Override
    public void handle(Message message) throws IOException {
        FollowUserEvent event = objectMapper.readValue(message.getBody(), FollowUserEvent.class);

        String followingId = event.getFollowingId();
        String followerId = event.getFollowerId();
        String followeeId = event.getFolloweeId();
        Instant timestamp = event.getTimestamp();

        notificationService.createFollowNotification(followingId, followerId, followeeId, timestamp);
    }

    @Override
    public void undo(Message message) throws IOException {
        FollowUserEvent event = objectMapper.readValue(message.getBody(), FollowUserEvent.class);
        String followingId = event.getFollowingId();

        notificationService.undoFollowNotification(followingId);
    }
}

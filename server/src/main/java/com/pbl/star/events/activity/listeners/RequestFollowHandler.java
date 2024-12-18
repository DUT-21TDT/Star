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
public class RequestFollowHandler implements UserActivityHandler {

    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;

    public RequestFollowHandler(NotificationService notificationService) {
        this.objectMapper = new JacksonConfig().queueObjectMapper();
        this.notificationService = notificationService;
    }

    @Override
    public String[] getRoutingKey() {
        return new String[]{"notification.request_follow", "notification.UNDO_request_follow"};
    }

    @Override
    public void handle(Message message) throws IOException {
        FollowUserEvent event = objectMapper.readValue(message.getBody(), FollowUserEvent.class);

        String followingId = event.getFollowingId();
        String followerId = event.getFollowerId();
        String followeeId = event.getFolloweeId();
        Instant timestamp = event.getTimestamp();

        notificationService.createFollowUserNotification(followingId, followerId, followeeId, timestamp, NotificationType.REQUEST_FOLLOW);
    }

    @Override
    public void undo(Message message) throws IOException {
        FollowUserEvent event = objectMapper.readValue(message.getBody(), FollowUserEvent.class);
        String followingId = event.getFollowingId();

        notificationService.undoFollowUserNotification(followingId, NotificationType.REQUEST_FOLLOW);
    }
}

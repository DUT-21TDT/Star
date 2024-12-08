package com.pbl.star.events.activity.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbl.star.configurations.JacksonConfig;
import com.pbl.star.events.activity.NewPendingPostEvent;
import com.pbl.star.services.domain.NotificationService;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

@Component
public class NewPendingPostHandler implements UserActivityHandler {
    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;

    public NewPendingPostHandler(NotificationService notificationService) {
        this.objectMapper = new JacksonConfig().queueObjectMapper();
        this.notificationService = notificationService;
    }

    @Override
    public String[] getRoutingKey() {
        return new String[]{"notification.new_pending_post", "notification.UNDO_new_pending_post"};
    }

    @Override
    public void handle(Message message) throws IOException {
        NewPendingPostEvent event = objectMapper.readValue(message.getBody(), NewPendingPostEvent.class);

        String actorId = event.getActorId();
        Instant timestamp = event.getTimestamp();
        String roomId = event.getRoomId();

        notificationService.createNewPostNotification(roomId, actorId, timestamp);
    }

    @Override
    public void undo(Message message) throws IOException {
        NewPendingPostEvent event = objectMapper.readValue(message.getBody(), NewPendingPostEvent.class);

        String actorId = event.getActorId();
        String roomId = event.getRoomId();

        notificationService.undoNewPostNotification(roomId, actorId);
    }
}

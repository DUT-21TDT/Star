package com.pbl.star.events.notification.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbl.star.configurations.JacksonConfig;
import com.pbl.star.events.notification.NewPendingPostEvent;
import com.pbl.star.services.domain.NotificationService;
import com.pbl.star.services.domain.UserRoomService;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

@Component
public class NewPendingPostHandler implements UserActivityHandler {
    private final ObjectMapper objectMapper;
    private final UserRoomService userRoomService;
    private final NotificationService notificationService;

    public NewPendingPostHandler(UserRoomService userRoomService, NotificationService notificationService) {
        this.objectMapper = new JacksonConfig().queueObjectMapper();
        this.userRoomService = userRoomService;
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

        List<String> receivers = userRoomService.getModeratorIds(roomId);

        if (receivers.isEmpty()) {
            return;
        }

        notificationService.createNewPostNotification(roomId, actorId, timestamp, receivers);
    }

    @Override
    public void undo(Message message) throws IOException {
        NewPendingPostEvent event = objectMapper.readValue(message.getBody(), NewPendingPostEvent.class);

        String actorId = event.getActorId();
        String roomId = event.getRoomId();

        notificationService.undoNewPostNotification(roomId, actorId);
    }
}

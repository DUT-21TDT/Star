package com.pbl.star.events.activity.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbl.star.configurations.JacksonConfig;
import com.pbl.star.events.activity.InteractPostEvent;
import com.pbl.star.services.domain.NotificationService;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

import static com.pbl.star.enums.NotificationType.REPOST_POST;

@Component
public class RepostPostHandler implements UserActivityHandler {

    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;

    public RepostPostHandler(NotificationService notificationService) {
        this.objectMapper = new JacksonConfig().queueObjectMapper();
        this.notificationService = notificationService;
    }

    @Override
    public String[] getRoutingKey() {
        return new String[]{"notification.repost_post", "notification.UNDO_repost_post"};
    }

    @Override
    public void handle(Message message) throws IOException {

        InteractPostEvent event = objectMapper.readValue(message.getBody(), InteractPostEvent.class);

        String postId = event.getPostId();
        String actorId = event.getActorId();
        Instant timestamp = event.getTimestamp();

        notificationService.createInteractPostNotification(postId, actorId, timestamp, REPOST_POST);
    }

    @Override
    public void undo(Message message) throws IOException {
        InteractPostEvent event = objectMapper.readValue(message.getBody(), InteractPostEvent.class);

        String postId = event.getPostId();
        String actorId = event.getActorId();

        notificationService.undoInteractPostNotification(postId, actorId, REPOST_POST);
    }
}

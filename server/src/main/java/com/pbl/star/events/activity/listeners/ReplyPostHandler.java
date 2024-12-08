package com.pbl.star.events.activity.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbl.star.configurations.JacksonConfig;
import com.pbl.star.enums.NotificationType;
import com.pbl.star.events.activity.InteractPostEvent;
import com.pbl.star.services.domain.NotificationService;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

@Component
public class ReplyPostHandler implements UserActivityHandler {

    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;

    public ReplyPostHandler(NotificationService notificationService) {
        this.objectMapper = new JacksonConfig().queueObjectMapper();
        this.notificationService = notificationService;
    }

    @Override
    public String[] getRoutingKey() {
        return new String[]{"notification.reply_post", "notification.UNDO_reply_post"};
    }

    @Override
    public void handle(Message message) throws IOException {

        InteractPostEvent event = objectMapper.readValue(message.getBody(), InteractPostEvent.class);

        String postId = event.getPostId();
        String actorId = event.getActorId();
        Instant timestamp = event.getTimestamp();

        notificationService.createInteractPostNotification(postId, actorId, timestamp, NotificationType.REPLY_POST);
    }

    @Override
    public void undo(Message message) throws IOException {
        InteractPostEvent event = objectMapper.readValue(message.getBody(), InteractPostEvent.class);

        String parentPostId = event.getPostId();
        String actorId = event.getActorId();

        notificationService.undoInteractPostNotification(parentPostId, actorId, NotificationType.REPLY_POST);
    }
}

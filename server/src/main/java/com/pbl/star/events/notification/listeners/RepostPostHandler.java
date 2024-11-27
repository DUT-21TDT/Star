package com.pbl.star.events.notification.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbl.star.configurations.JacksonConfig;
import com.pbl.star.entities.Post;
import com.pbl.star.events.notification.InteractPostEvent;
import com.pbl.star.services.domain.NotificationService;
import com.pbl.star.services.domain.PostService;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

@Component
public class RepostPostHandler implements UserActivityHandler {

    private final ObjectMapper objectMapper;
    private final PostService postService;
    private final NotificationService notificationService;

    public RepostPostHandler(PostService postService, NotificationService notificationService) {
        this.objectMapper = new JacksonConfig().queueObjectMapper();
        this.postService = postService;
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

        Post post = postService.findExistPostById(postId).orElse(null);

        if (post == null) return;

        String receiverId = post.getUserId();

        if (!actorId.equals(receiverId)) {
            notificationService.createRepostPostNotification(postId, actorId, timestamp, receiverId);
        }
    }

    @Override
    public void undo(Message message) throws IOException {
        InteractPostEvent event = objectMapper.readValue(message.getBody(), InteractPostEvent.class);

        String postId = event.getPostId();
        String actorId = event.getActorId();

        notificationService.undoRepostPostNotification(postId, actorId);
    }
}

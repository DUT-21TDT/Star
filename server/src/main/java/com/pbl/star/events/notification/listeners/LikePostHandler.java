package com.pbl.star.events.notification.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbl.star.entities.Post;
import com.pbl.star.events.notification.LikePostEvent;
import com.pbl.star.services.domain.NotificationService;
import com.pbl.star.services.domain.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

@Component
@RequiredArgsConstructor
public class LikePostHandler implements UserActivityHandler {

    private final ObjectMapper objectMapper;
    private final PostService postService;
    private final NotificationService notificationService;

    @Override
    public String getRoutingKey() {
        return "notification.like_post";
    }

    @Override
    public void handle(Message message) throws IOException {

        LikePostEvent event = objectMapper.readValue(message.getBody(), LikePostEvent.class);

        String postId = event.getPostId();
        String actorId = event.getActorId();
        Instant timestamp = event.getTimestamp();

        Post post = postService.findExistPostById(postId).orElse(null);

        if (post == null) return;

        String receiverId = post.getUserId();

        if (!actorId.equals(receiverId)) {
            notificationService.createLikePostNotification(postId, actorId, timestamp, receiverId);
        }
    }
}

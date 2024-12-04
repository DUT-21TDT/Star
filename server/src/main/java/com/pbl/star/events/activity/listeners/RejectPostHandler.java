package com.pbl.star.events.activity.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbl.star.configurations.JacksonConfig;
import com.pbl.star.entities.Post;
import com.pbl.star.events.activity.ModeratePostEvent;
import com.pbl.star.services.domain.NotificationService;
import com.pbl.star.services.domain.PostService;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

@Component
public class RejectPostHandler implements UserActivityHandler {

    private final ObjectMapper objectMapper;
    private final PostService postService;
    private final NotificationService notificationService;

    public RejectPostHandler(PostService postService, NotificationService notificationService) {
        this.objectMapper = new JacksonConfig().queueObjectMapper();
        this.postService = postService;
        this.notificationService = notificationService;
    }
    @Override
    public String[] getRoutingKey() {
        return new String[]{"notification.reject_post"};
    }

    @Override
    public void handle(Message message) throws IOException {
        ModeratePostEvent event = objectMapper.readValue(message.getBody(), ModeratePostEvent.class);
        String postId = event.getPostId();
        Instant timestamp = event.getTimestamp();

        Post post = postService.findExistPostById(postId).orElse(null);

        if (post == null) return;

        notificationService.createRejectPostNotification(postId, timestamp, post.getUserId());
    }

    @Override
    public void undo(Message message) throws IOException {

    }
}

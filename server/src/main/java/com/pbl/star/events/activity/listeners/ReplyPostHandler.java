package com.pbl.star.events.activity.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbl.star.configurations.JacksonConfig;
import com.pbl.star.entities.Post;
import com.pbl.star.events.activity.InteractPostEvent;
import com.pbl.star.services.domain.NotificationService;
import com.pbl.star.services.domain.PostService;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

@Component
public class ReplyPostHandler implements UserActivityHandler {

    private final ObjectMapper objectMapper;
    private final PostService postService;
    private final NotificationService notificationService;

    public ReplyPostHandler(PostService postService, NotificationService notificationService) {
        this.objectMapper = new JacksonConfig().queueObjectMapper();
        this.postService = postService;
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

        Post post = postService.findExistPostById(postId).orElse(null);

        if (post == null) return;

        String receiverId = post.getUserId();

        if (!actorId.equals(receiverId)) {
            notificationService.createReplyPostNotification(postId, actorId, timestamp, receiverId);
        }
    }

    @Override
    public void undo(Message message) throws IOException {
        InteractPostEvent event = objectMapper.readValue(message.getBody(), InteractPostEvent.class);

        String parentPostId = event.getPostId();
        String actorId = event.getActorId();

        notificationService.undoReplyPostNotification(parentPostId, actorId);
    }
}

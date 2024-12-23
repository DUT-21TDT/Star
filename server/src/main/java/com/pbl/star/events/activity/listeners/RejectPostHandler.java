package com.pbl.star.events.activity.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbl.star.configurations.JacksonConfig;
import com.pbl.star.dtos.response.notification.NotificationForUserResponse;
import com.pbl.star.enums.NotificationType;
import com.pbl.star.events.activity.ModeratePostEvent;
import com.pbl.star.mapper.notification.NotificationDTOMapper;
import com.pbl.star.models.entities.Notification;
import com.pbl.star.models.projections.notification.NotificationForUser;
import com.pbl.star.services.domain.NotificationService;
import com.pbl.star.services.external.SSEManager;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

@Component
public class RejectPostHandler implements UserActivityHandler {

    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;
    private final SSEManager sseManager;
    private final NotificationDTOMapper mapper;

    public RejectPostHandler(NotificationService notificationService, SSEManager sseManager, NotificationDTOMapper mapper) {
        this.objectMapper = new JacksonConfig().queueObjectMapper();
        this.notificationService = notificationService;
        this.sseManager = sseManager;
        this.mapper = mapper;
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

        Notification noti = notificationService.createModeratePostNotification(postId, timestamp, NotificationType.REJECT_POST);

        if (noti != null) {
            NotificationForUser pushedNoti = notificationService.getPushedNotification(noti.getNotificationObjectId());
            NotificationForUserResponse pushedNotiRes = mapper.toDTO(pushedNoti);
            sseManager.sendNotification(noti.getReceiverId(), pushedNotiRes);
        }
    }

    @Override
    public void undo(Message message) throws IOException {

    }
}

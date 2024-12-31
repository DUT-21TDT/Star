package com.pbl.star.events.activity.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbl.star.configurations.JacksonConfig;
import com.pbl.star.events.activity.InteractPostEvent;
import com.pbl.star.mapper.notification.NotificationDTOMapper;
import com.pbl.star.models.entities.Notification;
import com.pbl.star.services.domain.NotificationService;
import com.pbl.star.services.external.SSEManager;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

@Component
public class ReportPostHandler implements UserActivityHandler {

    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;
    private final SSEManager sseManager;
    private final NotificationDTOMapper mapper;

    public ReportPostHandler(NotificationService notificationService, SSEManager sseManager, NotificationDTOMapper mapper) {
        this.objectMapper = new JacksonConfig().queueObjectMapper();
        this.notificationService = notificationService;
        this.sseManager = sseManager;
        this.mapper = mapper;
    }
    @Override
    public String[] getRoutingKey() {
        return new String[]{"notification.report_post", "notification.UNDO_report_post"};
    }

    @Override
    public void handle(Message message) throws IOException {
        InteractPostEvent event = objectMapper.readValue(message.getBody(), InteractPostEvent.class);

        String postId = event.getPostId();
        String actorId = event.getActorId();
        Instant timestamp = event.getTimestamp();

        List<Notification> notis = notificationService.reportPostNotification(postId, actorId, timestamp);

//        if (noti != null) {
//            NotificationForUser pushedNoti = notificationService.getPushedNotification(noti.getNotificationObjectId());
//            NotificationForUserResponse pushedNotiRes = mapper.toDTO(pushedNoti);
//            sseManager.sendNotification(noti.getReceiverId(), pushedNotiRes);
//        }
    }

    @Override
    public void undo(Message message) throws IOException {

    }
}

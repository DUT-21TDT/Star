package com.pbl.star.events.activity.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbl.star.configurations.JacksonConfig;
import com.pbl.star.events.activity.NewPendingPostEvent;
import com.pbl.star.mapper.notification.NotificationDTOMapper;
import com.pbl.star.models.entities.Notification;
import com.pbl.star.services.domain.NotificationService;
import com.pbl.star.services.external.PredictCallService;
import com.pbl.star.services.external.SSEManager;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

@Component
public class NewPendingPostHandler implements UserActivityHandler {
    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;
    private final SSEManager sseManager;
    private final NotificationDTOMapper mapper;
    private final PredictCallService predictCallService;

    public NewPendingPostHandler(NotificationService notificationService, SSEManager sseManager, NotificationDTOMapper mapper, PredictCallService predictCallService) {
        this.objectMapper = new JacksonConfig().queueObjectMapper();
        this.notificationService = notificationService;
        this.sseManager = sseManager;
        this.mapper = mapper;
        this.predictCallService = predictCallService;
    }

    @Override
    public String[] getRoutingKey() {
        return new String[]{"notification.new_pending_post", "notification.UNDO_new_pending_post"};
    }

    @Override
    public void handle(Message message) throws IOException {
        NewPendingPostEvent event = objectMapper.readValue(message.getBody(), NewPendingPostEvent.class);

        String postId = event.getPostId();
        String actorId = event.getActorId();
        Instant timestamp = event.getTimestamp();
        String roomId = event.getRoomId();

        List<Notification> notis = notificationService.createNewPostNotification(roomId, actorId, timestamp);

        predictCallService.callPredictAsync(postId);
//        if (notis == null || notis.isEmpty()) {
//            return;
//        }

//        for (Notification noti : notis) {
//            NotificationForUser pushedNoti = notificationService.getPushedNotification(noti.getNotificationObjectId());
//            NotificationForUserResponse pushedNotiRes = mapper.toDTO(pushedNoti);
//            sseManager.sendNotification(noti.getReceiverId(), pushedNotiRes);
//        }
    }

    @Override
    public void undo(Message message) throws IOException {
        NewPendingPostEvent event = objectMapper.readValue(message.getBody(), NewPendingPostEvent.class);

        String actorId = event.getActorId();
        String roomId = event.getRoomId();

        notificationService.undoNewPostNotification(roomId, actorId);
    }
}

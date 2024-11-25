package com.pbl.star.services.external.impl;

import com.pbl.star.entities.PostLike;
import com.pbl.star.events.notification.LikePostEvent;
import com.pbl.star.services.external.NotificationProducer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationProducerImpl implements NotificationProducer {

    private final RabbitTemplate rabbitTemplate;
    private static final Logger logger = LoggerFactory.getLogger(NotificationProducerImpl.class);

    @Override
    public void pushLikePostEvent(PostLike postLike) {
        LikePostEvent event = new LikePostEvent();
        event.setTimestamp(postLike.getLikeAt());
        event.setPostId(postLike.getPostId());
        event.setActorId(postLike.getUserId());

        try {
            rabbitTemplate.convertAndSend("notification_exchange", "notification.like_post", event);
        } catch (Exception e) {
            logger.error("Failed to push like post event to notification service", e);
        }
    }
}

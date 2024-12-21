package com.pbl.star.services.external.impl;

import com.pbl.star.models.entities.Following;
import com.pbl.star.models.entities.Post;
import com.pbl.star.models.entities.PostLike;
import com.pbl.star.models.entities.PostRepost;
import com.pbl.star.events.activity.FollowUserEvent;
import com.pbl.star.events.activity.InteractPostEvent;
import com.pbl.star.events.activity.ModeratePostEvent;
import com.pbl.star.events.activity.NewPendingPostEvent;
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
    public void pushLikePostMessage(PostLike postLike) {
        InteractPostEvent event = new InteractPostEvent();
        event.setTimestamp(postLike.getLikeAt());
        event.setPostId(postLike.getPostId());
        event.setActorId(postLike.getUserId());

        try {
            rabbitTemplate.convertAndSend("notification_exchange", "notification.like_post", event);
        } catch (Exception e) {
            logger.error("Failed to push like post event to notification service", e);
        }
    }

    @Override
    public void pushUnlikePostMessage(String postId, String actorId) {
        InteractPostEvent event = new InteractPostEvent();
        event.setActorId(actorId);
        event.setPostId(postId);

        try {
            rabbitTemplate.convertAndSend("notification_exchange", "notification.UNDO_like_post", event);
        } catch (Exception e) {
            logger.error("Failed to push unlike post event to notification service", e);
        }
    }

    @Override
    public void pushReplyPostMessage(Post reply) {
        InteractPostEvent event = new InteractPostEvent();
        event.setTimestamp(reply.getCreatedAt());
        event.setPostId(reply.getParentPostId());
        event.setActorId(reply.getUserId());

        try {
            rabbitTemplate.convertAndSend("notification_exchange", "notification.reply_post", event);
        } catch (Exception e) {
            logger.error("Failed to push reply post event to notification service", e);
        }
    }

    @Override
    public void pushDeleteReplyMessage(String parentPostId, String actorId) {
        InteractPostEvent event = new InteractPostEvent();
        event.setActorId(actorId);
        event.setPostId(parentPostId);

        try {
            rabbitTemplate.convertAndSend("notification_exchange", "notification.UNDO_reply_post", event);
        } catch (Exception e) {
            logger.error("Failed to push delete reply post event to notification service", e);
        }
    }

    @Override
    public void pushRepostPostMessage(PostRepost postRepost) {
        InteractPostEvent event = new InteractPostEvent();
        event.setTimestamp(postRepost.getRepostAt());
        event.setPostId(postRepost.getPostId());
        event.setActorId(postRepost.getUserId());

        try {
            rabbitTemplate.convertAndSend("notification_exchange", "notification.repost_post", event);
        } catch (Exception e) {
            logger.error("Failed to push repost post event to notification service", e);
        }
    }

    @Override
    public void pushDeleteRepostPostMessage(String postId, String actorId) {
        InteractPostEvent event = new InteractPostEvent();
        event.setActorId(actorId);
        event.setPostId(postId);

        try {
            rabbitTemplate.convertAndSend("notification_exchange", "notification.UNDO_repost_post", event);
        } catch (Exception e) {
            logger.error("Failed to push undo repost post event to notification service", e);
        }
    }

    @Override
    public void pushFollowMessage(Following following) {
        FollowUserEvent event = new FollowUserEvent();
        event.setTimestamp(following.getFollowAt());
        event.setFollowerId(following.getFollowerId());
        event.setFolloweeId(following.getFolloweeId());
        event.setFollowingId(following.getId());

        try {
            rabbitTemplate.convertAndSend("notification_exchange", "notification.follow", event);
        } catch (Exception e) {
            logger.error("Failed to push follow user event to notification service", e);
        }
    }

    @Override
    public void pushUnfollowMessage(Following following) {
        FollowUserEvent event = new FollowUserEvent();
        event.setFollowingId(following.getId());

        try {
            rabbitTemplate.convertAndSend("notification_exchange", "notification.UNDO_follow", event);
        } catch (Exception e) {
            logger.error("Failed to push unfollow user event to notification service", e);
        }
    }

    @Override
    public void pushRequestFollowMessage(Following following) {
        FollowUserEvent event = new FollowUserEvent();
        event.setTimestamp(following.getFollowAt());
        event.setFollowerId(following.getFollowerId());
        event.setFolloweeId(following.getFolloweeId());
        event.setFollowingId(following.getId());

        try {
            rabbitTemplate.convertAndSend("notification_exchange", "notification.request_follow", event);
        } catch (Exception e) {
            logger.error("Failed to push request follow event to notification service", e);
        }
    }

    @Override
    public void pushRevokeRequestFollowMessage(Following following) {
        FollowUserEvent event = new FollowUserEvent();
        event.setFollowingId(following.getId());

        try {
            rabbitTemplate.convertAndSend("notification_exchange", "notification.UNDO_request_follow", event);
        } catch (Exception e) {
            logger.error("Failed to push unfollow user event to notification service", e);
        }
    }

    @Override
    public void pushAcceptFollowMessage(Following following) {
        FollowUserEvent event = new FollowUserEvent();
        event.setTimestamp(following.getFollowAt());
        event.setFollowerId(following.getFollowerId());
        event.setFolloweeId(following.getFolloweeId());
        event.setFollowingId(following.getId());

        try {
            rabbitTemplate.convertAndSend("notification_exchange", "notification.accept_follow", event);
        } catch (Exception e) {
            logger.error("Failed to push accept follow event to notification service", e);
        }
    }

    @Override
    public void pushApprovePostMessage(Post post) {
        ModeratePostEvent event = new ModeratePostEvent();
        event.setPostId(post.getId());
        event.setTimestamp(post.getCreatedAt());

        try {
            rabbitTemplate.convertAndSend("notification_exchange", "notification.approve_post", event);
        } catch (Exception e) {
            logger.error("Failed to push approve post event to notification service", e);
        }
    }

    @Override
    public void pushRejectPostMessage(Post post) {
        ModeratePostEvent event = new ModeratePostEvent();
        event.setPostId(post.getId());
        event.setTimestamp(post.getCreatedAt());

        try {
            rabbitTemplate.convertAndSend("notification_exchange", "notification.reject_post", event);
        } catch (Exception e) {
            logger.error("Failed to push reject post event to notification service", e);
        }
    }

    @Override
    public void pushNewPendingPostMessage(Post post) {
        NewPendingPostEvent event = new NewPendingPostEvent();
        event.setActorId(post.getUserId());
        event.setRoomId(post.getRoomId());
        event.setTimestamp(post.getCreatedAt());

        try {
            rabbitTemplate.convertAndSend("notification_exchange", "notification.new_pending_post", event);
        } catch (Exception e) {
            logger.error("Failed to push new pending post event to notification service", e);
        }
    }

    @Override
    public void pushRemovePendingPostMessage(Post post) {
        NewPendingPostEvent event = new NewPendingPostEvent();
        event.setActorId(post.getUserId());
        event.setRoomId(post.getRoomId());

        try {
            rabbitTemplate.convertAndSend("notification_exchange", "notification.UNDO_new_pending_post", event);
        } catch (Exception e) {
            logger.error("Failed to push remove pending post event to notification service", e);
        }
    }
}

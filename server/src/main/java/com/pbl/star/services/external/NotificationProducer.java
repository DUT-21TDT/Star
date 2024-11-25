package com.pbl.star.services.external;

import com.pbl.star.entities.PostLike;

public interface NotificationProducer {
    void pushLikePostEvent(PostLike postLike);
}

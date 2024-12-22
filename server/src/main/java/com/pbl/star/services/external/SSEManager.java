package com.pbl.star.services.external;

import com.pbl.star.dtos.response.notification.NotificationForUserResponse;
import reactor.core.publisher.Sinks;

public interface SSEManager {
    Sinks.Many<String> getUserSink(String userId);
    void removeUserSink(String userId);
    void sendNotification(String userId, NotificationForUserResponse notification);
}

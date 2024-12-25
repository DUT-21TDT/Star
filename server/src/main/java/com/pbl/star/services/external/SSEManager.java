package com.pbl.star.services.external;

import com.pbl.star.dtos.response.notification.NotificationForUserResponse;
import org.springframework.lang.NonNull;
import reactor.core.publisher.Sinks;

public interface SSEManager {
    Sinks.Many<String> getUserSink(String userId);
    void removeUserSink(String userId);
    void sendNotification(@NonNull String userId, NotificationForUserResponse notification);
}

package com.pbl.star.services.external;

import com.pbl.star.dtos.response.notification.NotificationForUserResponse;
import org.springframework.lang.NonNull;
import reactor.core.publisher.Flux;

public interface SSEManager {
    Flux<String> getUserFlux(String userId);
    void sendNotification(@NonNull String userId, NotificationForUserResponse notification);
}

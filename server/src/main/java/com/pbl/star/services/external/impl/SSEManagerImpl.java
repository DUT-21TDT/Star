package com.pbl.star.services.external.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbl.star.dtos.response.notification.NotificationForUserResponse;
import com.pbl.star.services.external.SSEManager;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class SSEManagerImpl implements SSEManager {

    private final ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(NotificationProducerImpl.class);

    // Map để quản lý các kết nối SSE theo userId
    private final ConcurrentHashMap<String, FluxSink<String>> subscribers = new ConcurrentHashMap<>();

    public Flux<String> getUserFlux(String userId) {
        return Flux.create(emitter -> {
            subscribers.put(userId, emitter);
            emitter.onDispose(() -> subscribers.remove(userId));
        });    }

    @Override
    public void removeUser(String userId) {
        subscribers.remove(userId);
    }

    public void sendNotification(@NonNull String userId, NotificationForUserResponse notification) {
        FluxSink<String> flux = subscribers.get(userId);

        if (flux != null) {
            try {
                if (notification == null) {
                    flux.next(": keep-alive\n\n"); // Send a comment as keep-alive signal
                }
                String jsonNotification = objectMapper.writeValueAsString(notification);
                flux.next("data: " + jsonNotification + "\n\n");
            } catch (Exception e) {
                logger.error("Error when sending notification to user " + userId, e);
            }
        }
    }
}

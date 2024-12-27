package com.pbl.star.services.external.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbl.star.dtos.response.notification.NotificationForUserResponse;
import com.pbl.star.services.external.SSEManager;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class SSEManagerImpl implements SSEManager {

    private final ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(NotificationProducerImpl.class);

    // Map để quản lý các kết nối SSE theo userId
    private final Map<String, Sinks.Many<String>> userConnections = new ConcurrentHashMap<>();

    public Sinks.Many<String> getUserSink(String userId) {
        return userConnections.computeIfAbsent(userId, k -> Sinks.many().multicast().onBackpressureBuffer());
    }

    public void removeUserSink(String userId) {
        userConnections.remove(userId);
    }

    public void sendNotification(@NonNull String userId, NotificationForUserResponse notification) {
        Sinks.Many<String> sink = userConnections.get(userId);

        if (sink != null) {
            try {
                if (notification == null) {
                    sink.tryEmitNext("");
                }
                String jsonNotification = objectMapper.writeValueAsString(notification);
                Sinks.EmitResult result = sink.tryEmitNext(jsonNotification);
                if (result.isFailure()) {
                    logger.warn("Failed to emit notification to user {}: {}", userId, result);
                }
            } catch (Exception e) {
                logger.error("Error when sending notification to user " + userId, e);
            }
        }
    }
}

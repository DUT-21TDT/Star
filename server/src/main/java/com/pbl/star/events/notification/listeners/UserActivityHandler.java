package com.pbl.star.events.notification.listeners;

import org.springframework.amqp.core.Message;

import java.io.IOException;

public interface UserActivityHandler {
    String getRoutingKey();
    void handle(Message message) throws IOException;
}

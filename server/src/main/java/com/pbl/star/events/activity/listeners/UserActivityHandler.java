package com.pbl.star.events.activity.listeners;

import org.springframework.amqp.core.Message;

import java.io.IOException;

public interface UserActivityHandler {
    String[] getRoutingKey();
    void handle(Message message) throws IOException;
    void undo(Message message) throws IOException;
}

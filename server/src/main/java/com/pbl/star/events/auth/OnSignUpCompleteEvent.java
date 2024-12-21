package com.pbl.star.events.auth;

import com.pbl.star.models.entities.User;
import lombok.*;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
@Builder
public class OnSignUpCompleteEvent extends ApplicationEvent {
    private final User user;

    public OnSignUpCompleteEvent(User user) {
        super(user);
        this.user = user;
    }
}

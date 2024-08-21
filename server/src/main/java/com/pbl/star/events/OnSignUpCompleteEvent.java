package com.pbl.star.events;

import com.pbl.star.entities.User;
import lombok.*;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
@Builder
public class OnSignUpCompleteEvent extends ApplicationEvent {
    private User user;

    public OnSignUpCompleteEvent(User user) {
        super(user);
        this.user = user;
    }
}

package com.pbl.star.events.auth;

import com.pbl.star.models.entities.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
@Builder
public class OnConfirmSignUpEvent extends ApplicationEvent {
    private final User user;

    public OnConfirmSignUpEvent(User user) {
        super(user);
        this.user = user;
    }
}

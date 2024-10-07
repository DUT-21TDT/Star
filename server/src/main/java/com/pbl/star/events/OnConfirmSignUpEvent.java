package com.pbl.star.events;

import com.pbl.star.entities.User;
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

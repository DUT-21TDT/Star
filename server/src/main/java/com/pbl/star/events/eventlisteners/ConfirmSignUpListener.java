package com.pbl.star.events.eventlisteners;

import com.pbl.star.events.OnConfirmSignUpEvent;

public interface ConfirmSignUpListener {
    void removeVerificationToken(OnConfirmSignUpEvent event);
}

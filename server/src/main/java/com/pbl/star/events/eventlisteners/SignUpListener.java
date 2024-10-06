package com.pbl.star.events.eventlisteners;

import com.pbl.star.events.OnSignUpCompleteEvent;

public interface SignUpListener {
    void sendConfirmationEmail(OnSignUpCompleteEvent event);
}

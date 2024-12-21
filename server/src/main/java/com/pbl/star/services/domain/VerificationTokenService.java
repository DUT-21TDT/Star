package com.pbl.star.services.domain;

import com.pbl.star.models.entities.User;
import com.pbl.star.models.entities.VerificationToken;

public interface VerificationTokenService {
    VerificationToken createVerificationToken(User user);
    void removeVerificationToken(User user);
}

package com.pbl.star.services;

import com.pbl.star.entities.User;
import com.pbl.star.entities.VerificationToken;

public interface VerificationTokenService {
    VerificationToken createVerificationToken(User user);
    void removeVerificationToken(User user);
}

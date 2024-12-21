package com.pbl.star.services.domain;

import com.pbl.star.dtos.request.user.SignUpParams;
import com.pbl.star.models.entities.User;

public interface AuthService {
    User signUpByEmail(SignUpParams signUpParams);
    User confirmSignup(String token);
    User handleResendConfirmation(String userId, String email);
}

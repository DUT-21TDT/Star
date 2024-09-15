package com.pbl.star.services;

import com.pbl.star.dtos.request.user.SignUpParams;
import com.pbl.star.dtos.response.user.ConfirmSignUpResponse;
import com.pbl.star.entities.User;
import com.pbl.star.entities.VerificationToken;

public interface AuthService {
    User signUpByEmail(SignUpParams signUpParams);
    ConfirmSignUpResponse confirmSignup(String token);
    VerificationToken createVerificationToken(User user);
}

package com.pbl.star.services;

import com.pbl.star.dtos.request.auth.SignUpParams;
import com.pbl.star.dtos.response.auth.ConfirmSignUpResponse;
import com.pbl.star.entities.User;
import com.pbl.star.entities.VerificationToken;

public interface AuthService {
    User signUpByEmail(SignUpParams signUpParams);
    ConfirmSignUpResponse confirmSignup(String token);
    VerificationToken createVerificationToken(User user);
}

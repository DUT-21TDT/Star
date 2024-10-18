package com.pbl.star.usecase;

import com.pbl.star.dtos.request.user.SignUpParams;
import com.pbl.star.dtos.response.user.ConfirmSignUpResponse;
import com.pbl.star.dtos.response.user.SignUpResponse;

public interface AuthUsecase {
    SignUpResponse signUpByEmail(SignUpParams signUpParams);
    ConfirmSignUpResponse confirmSignup(String token);
    void resendConfirmation(String email);
}

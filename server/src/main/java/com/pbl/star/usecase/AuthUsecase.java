package com.pbl.star.usecase;

import com.pbl.star.dtos.request.auth.SignUpParams;
import com.pbl.star.dtos.response.auth.ConfirmSignUpResponse;
import com.pbl.star.dtos.response.auth.SignUpResponse;

public interface AuthUsecase {
    SignUpResponse signUpByEmail(SignUpParams signUpParams);
    ConfirmSignUpResponse confirmSignup(String token);
}

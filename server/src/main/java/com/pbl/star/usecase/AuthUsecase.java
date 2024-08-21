package com.pbl.star.usecase;

import com.pbl.star.dtos.request.SignUpParams;
import com.pbl.star.dtos.response.ConfirmSignUpResponse;
import com.pbl.star.dtos.response.SignUpResponse;

public interface AuthUsecase {
    SignUpResponse signUpByEmail(SignUpParams signUpParams);
    ConfirmSignUpResponse confirmSignup(String token);
}

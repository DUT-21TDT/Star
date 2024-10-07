package com.pbl.star.services;

import com.pbl.star.dtos.request.user.SignUpParams;
import com.pbl.star.entities.User;

public interface AuthService {
    User signUpByEmail(SignUpParams signUpParams);
    User confirmSignup(String token);
}

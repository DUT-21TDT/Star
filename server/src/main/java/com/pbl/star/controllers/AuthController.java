package com.pbl.star.controllers;

import com.pbl.star.dtos.request.user.ResendEmailParams;
import com.pbl.star.dtos.request.user.SignUpParams;
import com.pbl.star.usecase.common.AuthUsecase;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Validated
public class AuthController {

    private final AuthUsecase authUsecase;

    public AuthController(AuthUsecase authUsecase) {
        this.authUsecase = authUsecase;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpParams signUpParams) {
        return ResponseEntity.ok(authUsecase.signUpByEmail(signUpParams));
    }

    @GetMapping("/confirm-signup")
    public ResponseEntity<?> confirmSignup(@RequestParam String token) {
        return ResponseEntity.ok(authUsecase.confirmSignup(token));
    }

    @PostMapping("/resend-confirmation")
    public ResponseEntity<?> resendConfirmation(@RequestBody(required = false) ResendEmailParams resendEmailParams) {
        authUsecase.resendConfirmation(resendEmailParams.getEmail());
        return ResponseEntity.ok().build();
    }
}

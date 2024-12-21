package com.pbl.star.controllers;

import com.pbl.star.dtos.request.user.ResendEmailParams;
import com.pbl.star.dtos.request.user.SignUpParams;
import com.pbl.star.usecase.shared.AuthUsecase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthUsecase authUsecase;

    public AuthController(AuthUsecase authUsecase) {
        this.authUsecase = authUsecase;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signup(@RequestBody @Valid SignUpParams signUpParams) {
        return ResponseEntity.ok(authUsecase.signUpByEmail(signUpParams));
    }

    @GetMapping("/confirm-sign-up")
    public ResponseEntity<?> confirmSignup(@RequestParam @NotBlank String token) {
        return ResponseEntity.ok(authUsecase.confirmSignup(token));
    }

    @PostMapping("/resend-confirmation")
    public ResponseEntity<?> resendConfirmation(@RequestBody(required = false) @Valid ResendEmailParams resendEmailParams) {
        authUsecase.resendConfirmation(resendEmailParams.getEmail());
        return ResponseEntity.ok().build();
    }
}

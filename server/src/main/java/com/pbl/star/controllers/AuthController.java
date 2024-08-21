package com.pbl.star.controllers;

import com.pbl.star.dtos.request.SignUpParams;
import com.pbl.star.services.AuthService;
import com.pbl.star.usecase.AuthUsecase;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthUsecase authUsecase;

    public AuthController(AuthUsecase authUsecase) {
        this.authUsecase = authUsecase;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpParams signUpParams) {
        return ResponseEntity.ok(authUsecase.signUpByEmail(signUpParams));
    }

    @GetMapping("/confirm-signup")
    public ResponseEntity<?> confirmSignup(@RequestParam String token) {
        return ResponseEntity.ok(authUsecase.confirmSignup(token));
    }
}

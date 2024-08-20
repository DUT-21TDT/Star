package com.pbl.star.controllers;

import com.pbl.star.dtos.request.SignUpParams;
import com.pbl.star.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpParams signUpParams) {
        return ResponseEntity.ok(authService.signUpByEmail(signUpParams));
    }

    @GetMapping("/confirm-signup")
    public ResponseEntity<?> confirmSignup(@RequestParam String token) {
        return ResponseEntity.ok(authService.confirmSignup(token));
    }
}

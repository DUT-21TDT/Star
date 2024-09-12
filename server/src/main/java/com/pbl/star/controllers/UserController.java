package com.pbl.star.controllers;

import com.pbl.star.usecase.ProfileManageUsecase;
import com.pbl.star.usecase.UserInteractUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserInteractUsecase userInteractUsecase;
    private final ProfileManageUsecase profileManageUsecase;

    @GetMapping("/{username}")
    public ResponseEntity<?> getPublicProfile(@PathVariable String username) {
        return ResponseEntity.ok(userInteractUsecase.getPublicProfile(username));
    }

    @GetMapping("/personal-information")
    public ResponseEntity<?> getPersonalInformation() {
        return ResponseEntity.ok(profileManageUsecase.getPersonalInformation());
    }
}

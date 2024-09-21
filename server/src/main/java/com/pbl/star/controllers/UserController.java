package com.pbl.star.controllers;

import com.pbl.star.dtos.request.user.UpdateProfileParams;
import com.pbl.star.usecase.ProfileManageUsecase;
import com.pbl.star.usecase.UserInteractUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserInteractUsecase userInteractUsecase;
    private final ProfileManageUsecase profileManageUsecase;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getPublicProfile(@PathVariable String userId) {
        return ResponseEntity.ok(userInteractUsecase.getPublicProfile(userId));
    }

    @GetMapping("/personal-information")
    public ResponseEntity<?> getPersonalInformation() {
        return ResponseEntity.ok(profileManageUsecase.getPersonalInformation());
    }

    @PatchMapping("/personal-information")
    public ResponseEntity<?> updatePersonalInformation(@ModelAttribute UpdateProfileParams updateProfileParams) {
        profileManageUsecase.updatePersonalInformation(updateProfileParams);
        return ResponseEntity.ok().build();
    }
}

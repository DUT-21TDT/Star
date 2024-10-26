package com.pbl.star.controllers;

import com.pbl.star.dtos.request.user.UpdateProfileParams;
import com.pbl.star.usecase.PostManageUsecase;
import com.pbl.star.usecase.ProfileManageUsecase;
import com.pbl.star.usecase.UserInteractUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile() {
        return ResponseEntity.ok(profileManageUsecase.getGeneralInformation());
    }

    @GetMapping("/me/personal-information")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> getPersonalInformation() {
        return ResponseEntity.ok(profileManageUsecase.getPersonalInformation());
    }

    @PatchMapping("/me/personal-information")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> updatePersonalInformation(@ModelAttribute UpdateProfileParams updateProfileParams) {
        return ResponseEntity.ok(profileManageUsecase.updatePersonalInformation(updateProfileParams));
    }
}

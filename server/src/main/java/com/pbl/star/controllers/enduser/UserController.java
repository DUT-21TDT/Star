package com.pbl.star.controllers.enduser;

import com.pbl.star.dtos.request.user.ChangePasswordParams;
import com.pbl.star.dtos.request.user.UpdateProfileParams;
import com.pbl.star.usecase.enduser.ManageProfileUsecase;
import com.pbl.star.usecase.enduser.InteractUserUsecase;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final InteractUserUsecase interactUserUsecase;
    private final ManageProfileUsecase manageProfileUsecase;

    @GetMapping
    public ResponseEntity<?> searchUsers(@RequestParam String keyword,
                                         @RequestParam(defaultValue = "20") @Min(1) @Max(100) int limit,
                                         @RequestParam(required = false) String afterId) {
        return ResponseEntity.ok(interactUserUsecase.searchUsers(keyword, limit, afterId));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getPublicProfile(@PathVariable String userId) {
        return ResponseEntity.ok(interactUserUsecase.getProfileOnWall(userId));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMyGeneralInformation() {
        return ResponseEntity.ok(manageProfileUsecase.getGeneralInformation());
    }

    @GetMapping("/me/personal-information")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> getPersonalInformation() {
        return ResponseEntity.ok(manageProfileUsecase.getPersonalInformation());
    }

    @PatchMapping("/me/personal-information")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> updatePersonalInformation(@ModelAttribute UpdateProfileParams updateProfileParams) {
        return ResponseEntity.ok(manageProfileUsecase.updatePersonalInformation(updateProfileParams));
    }

    @PutMapping("/me/password")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> updatePassword(@RequestBody ChangePasswordParams changePasswordParams) {
        manageProfileUsecase.changePassword(changePasswordParams);
        return ResponseEntity.ok().build();
    }
}

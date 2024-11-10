package com.pbl.star.controllers.enduser;

import com.pbl.star.dtos.request.user.UpdateProfileParams;
import com.pbl.star.usecase.ProfileManageUsecase;
import com.pbl.star.usecase.UserInteractUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserInteractUsecase userInteractUsecase;
    private final ProfileManageUsecase profileManageUsecase;

    @GetMapping
    public ResponseEntity<?> searchUsers(@RequestParam String keyword,
                                         @RequestParam(defaultValue = "20") int limit,
                                         @RequestParam(required = false) String afterId) {
        return ResponseEntity.ok(userInteractUsecase.searchUsers(keyword, limit, afterId));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getPublicProfile(@PathVariable String userId) {
        return ResponseEntity.ok(userInteractUsecase.getProfileOnWall(userId));
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

    @PostMapping("/me/followings")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> followUser(@RequestBody Map<String, String> requestBody) {
        String userId = requestBody.getOrDefault("userId", "");
        return ResponseEntity.ok(userInteractUsecase.followUser(userId));
    }

    @DeleteMapping("/me/followings/{userId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> unfollowUser(@PathVariable String userId) {
        userInteractUsecase.unfollowUser(userId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/me/follow-requests/{followingId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> acceptFollowRequest(@PathVariable String followingId) {
        userInteractUsecase.acceptFollowRequest(followingId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/me/follow-requests/{followingId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> rejectFollowRequest(@PathVariable String followingId) {
        userInteractUsecase.rejectFollowRequest(followingId);
        return ResponseEntity.ok().build();
    }
}

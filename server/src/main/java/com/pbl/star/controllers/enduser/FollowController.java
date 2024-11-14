package com.pbl.star.controllers.enduser;

import com.pbl.star.usecase.enduser.InteractUserUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class FollowController {

    private final InteractUserUsecase interactUserUsecase;

    @PostMapping("/me/followings")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> followUser(@RequestBody Map<String, String> requestBody) {
        String userId = requestBody.getOrDefault("userId", "");
        return ResponseEntity.ok(interactUserUsecase.followUser(userId));
    }

    @DeleteMapping("/me/followings/{userId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> unfollowUser(@PathVariable String userId) {
        interactUserUsecase.unfollowUser(userId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/me/follow-requests/{followingId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> acceptFollowRequest(@PathVariable String followingId) {
        interactUserUsecase.acceptFollowRequest(followingId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/me/follow-requests/{followingId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> rejectFollowRequest(@PathVariable String followingId) {
        interactUserUsecase.rejectFollowRequest(followingId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me/follow-requests")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> getFollowRequests() {
        return null;
//        return ResponseEntity.ok(interactUserUsecase.getFollowRequests());
    }

    @GetMapping("/{userId}/followings")
    public ResponseEntity<?> getFollowings(@PathVariable String userId,
                                           @RequestParam(defaultValue = "20") int limit,
                                           @RequestParam(required = false) Instant after
    ) {
        return ResponseEntity.ok(interactUserUsecase.getFollowings(userId, limit, after));
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<?> getFollowers(@PathVariable String userId,
                                          @RequestParam(defaultValue = "20") int limit,
                                          @RequestParam(required = false) Instant after) {
        return ResponseEntity.ok(interactUserUsecase.getFollowers(userId, limit, after));
    }
}

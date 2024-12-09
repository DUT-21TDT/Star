package com.pbl.star.controllers.enduser;

import com.pbl.star.usecase.enduser.ViewPostUsecase;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequiredArgsConstructor
@Validated
public class PostViewController {

    private final ViewPostUsecase viewPostUsecase;

    @GetMapping("/newsfeed/posts")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> getPostsOnNewsfeed(@RequestParam(defaultValue = "20") @Min(1) @Max(100) int limit,
                                                @RequestParam(required = false) Instant after) {
        return ResponseEntity.ok(viewPostUsecase.getPostsOnNewsfeed(limit, after));
    }

    @GetMapping("/followings/posts")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> getPostsOnFollowings(@RequestParam(defaultValue = "20") @Min(1) @Max(100) int limit,
                                                @RequestParam(required = false) Instant after) {
        return ResponseEntity.ok(viewPostUsecase.getPostsOfFollowingUsers(limit, after));
    }

    @GetMapping("/users/{userId}/posts")
    public ResponseEntity<?> getPostsByUser(@PathVariable String userId,
                                            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int limit,
                                            @RequestParam(required = false) Instant after) {
        return ResponseEntity.ok(viewPostUsecase.getPostsOnUserWall(userId, limit, after));
    }

    @GetMapping("/users/me/pending-posts")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> getPendingPostsByCurrentUser(@RequestParam(defaultValue = "20") @Min(1) @Max(100) int limit,
                                                   @RequestParam(required = false) Instant after) {
        return ResponseEntity.ok(viewPostUsecase.getMyPendingPosts(limit, after));
    }

    @GetMapping("/rooms/{roomId}/posts")
    public ResponseEntity<?> getPostsInRoom(@PathVariable String roomId,
                                           @RequestParam(defaultValue = "20") @Min(1) @Max(100) int limit,
                                           @RequestParam(required = false) Instant after) {
        return ResponseEntity.ok(viewPostUsecase.getPostsInRoomAsUser(roomId, limit, after));
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<?> getPostById(@PathVariable String postId) {
        return ResponseEntity.ok(viewPostUsecase.getPostById(postId));
    }

    @GetMapping("/posts/{postId}/replies")
    public ResponseEntity<?> getRepliesOfPost(@PathVariable String postId,
                                             @RequestParam(defaultValue = "20") @Min(1) @Max(100) int limit,
                                             @RequestParam(required = false) Instant after) {
        return ResponseEntity.ok(viewPostUsecase.getRepliesOfPost(postId, limit, after));
    }

    @GetMapping("/users/{userId}/replies")
    public ResponseEntity<?> getRepliesByUser(@PathVariable String userId,
                                            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int limit,
                                            @RequestParam(required = false) Instant after) {
        return ResponseEntity.ok(viewPostUsecase.getRepliesOnUserWall(userId, limit, after));
    }

    @GetMapping("/users/{userId}/reposts")
    public ResponseEntity<?> getRepostsByUser(@PathVariable String userId,
                                              @RequestParam(defaultValue = "20") @Min(1) @Max(100) int limit,
                                              @RequestParam(required = false) Instant after) {
        return ResponseEntity.ok(viewPostUsecase.getRepostsOnUserWall(userId, limit, after));
    }
}

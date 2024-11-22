package com.pbl.star.controllers.enduser;

import com.pbl.star.usecase.enduser.InteractPostUsecase;
import com.pbl.star.usecase.enduser.ManagePostUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequiredArgsConstructor
public class PostViewController {

    private final ManagePostUsecase managePostUsecase;
    private final InteractPostUsecase interactPostUsecase;

    @GetMapping("/newsfeed/posts")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> getPostsOnNewsfeed(@RequestParam(defaultValue = "20") int limit,
                                                @RequestParam(required = false) Instant after) {
        return ResponseEntity.ok(managePostUsecase.getPostsOnNewsfeed(limit, after));
    }

    @GetMapping("/users/{userId}/posts")
    public ResponseEntity<?> getPostsByUser(@PathVariable String userId,
                                            @RequestParam(defaultValue = "20") int limit,
                                            @RequestParam(required = false) Instant after) {
        return ResponseEntity.ok(managePostUsecase.getPostsOnUserWall(userId, limit, after));
    }

    @GetMapping("/users/me/pending-posts")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> getPendingPostsByCurrentUser(@RequestParam(defaultValue = "20") int limit,
                                                   @RequestParam(required = false) Instant after) {
        return ResponseEntity.ok(managePostUsecase.getMyPendingPosts(limit, after));
    }

    @GetMapping("/rooms/{roomId}/posts")
    public ResponseEntity<?> getPostsInRoom(@PathVariable String roomId,
                                           @RequestParam(defaultValue = "20") int limit,
                                           @RequestParam(required = false) Instant after) {
        return ResponseEntity.ok(managePostUsecase.getPostsInRoomAsUser(roomId, limit, after));
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<?> getPostById(@PathVariable String postId) {
        return ResponseEntity.ok(managePostUsecase.getPostById(postId));
    }

    @GetMapping("/posts/{postId}/replies")
    public ResponseEntity<?> getRepliesOfPost(@PathVariable String postId,
                                             @RequestParam(defaultValue = "20") int limit,
                                             @RequestParam(required = false) Instant after) {
        return ResponseEntity.ok(interactPostUsecase.getRepliesOfPost(postId, limit, after));
    }

    @GetMapping("/users/{userId}/replies")
    public ResponseEntity<?> getRepliesByUser(@PathVariable String userId,
                                            @RequestParam(defaultValue = "20") int limit,
                                            @RequestParam(required = false) Instant after) {
        return ResponseEntity.ok(managePostUsecase.getRepliesOnUserWall(userId, limit, after));
    }
}

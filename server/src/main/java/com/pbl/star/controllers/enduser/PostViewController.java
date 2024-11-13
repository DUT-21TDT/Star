package com.pbl.star.controllers.enduser;

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

    private final ManagePostUsecase postManageUsecase;

    @GetMapping("/newsfeed/posts")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> getPostsOnNewsfeed(@RequestParam(defaultValue = "20") int limit,
                                                @RequestParam(required = false) Instant after) {
        return ResponseEntity.ok(postManageUsecase.getPostsOnNewsfeed(limit, after));
    }

    @GetMapping("/users/{userId}/posts")
    public ResponseEntity<?> getPostsByUser(@PathVariable String userId,
                                            @RequestParam(defaultValue = "20") int limit,
                                            @RequestParam(required = false) Instant after) {
        return ResponseEntity.ok(postManageUsecase.getPostsOnUserWall(userId, limit, after));
    }

    @GetMapping("/users/me/pending-posts")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> getPendingPostsByCurrentUser(@RequestParam(defaultValue = "20") int limit,
                                                   @RequestParam(required = false) Instant after) {
        return ResponseEntity.ok(postManageUsecase.getMyPendingPosts(limit, after));
    }

    @GetMapping("/rooms/{roomId}/posts")
    public ResponseEntity<?> getPostsInRoom(@PathVariable String roomId,
                                           @RequestParam(defaultValue = "20") int limit,
                                           @RequestParam(required = false) Instant after) {
        return ResponseEntity.ok(postManageUsecase.getPostsInRoomAsUser(roomId, limit, after));
    }
}

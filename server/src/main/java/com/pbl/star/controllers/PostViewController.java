package com.pbl.star.controllers;

import com.pbl.star.enums.PostStatus;
import com.pbl.star.usecase.PostManageUsecase;
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

    private final PostManageUsecase postManageUsecase;

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

    @GetMapping("/rooms/{roomId}/posts")
    public ResponseEntity<?> getPostsInRoom(@PathVariable String roomId,
                                           @RequestParam(defaultValue = "APPROVED") PostStatus status,
                                           @RequestParam(defaultValue = "20") int limit,
                                           @RequestParam(required = false) Instant after) {

        return switch (status) {
            case APPROVED -> ResponseEntity.ok(postManageUsecase.getApprovedPostsInRoom(roomId, limit, after));
            case PENDING, REJECTED -> ResponseEntity.ok(postManageUsecase.getPostsInRoomAsMod(roomId, status, limit, after));
        };
    }
}

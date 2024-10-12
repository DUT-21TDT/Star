package com.pbl.star.controllers;

import com.pbl.star.dtos.request.post.CreatePostParams;
import com.pbl.star.usecase.PostManageUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostManageUsecase postManageUsecase;

    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> createPost(@RequestBody CreatePostParams createPostParams) {
        String postId = postManageUsecase.createPost(createPostParams);
        return ResponseEntity.ok(Map.of("id", postId));
    }

    @GetMapping("/newsfeed")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> getPostsOnNewsfeed(@RequestParam(defaultValue = "20") int limit,
                                                @RequestParam(required = false) Instant after) {
        return ResponseEntity.ok(postManageUsecase.getPostsOnNewsfeed(limit, after));
    }
}

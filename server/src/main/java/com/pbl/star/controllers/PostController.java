package com.pbl.star.controllers;

import com.pbl.star.dtos.request.post.CreatePostParams;
import com.pbl.star.usecase.PostManageUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostManageUsecase postManageUsecase;
    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody CreatePostParams createPostParams) {
        String postId = postManageUsecase.createPost(createPostParams);
        return ResponseEntity.ok(Map.of("id", postId));
    }
}

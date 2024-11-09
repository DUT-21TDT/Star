package com.pbl.star.controllers.enduser;

import com.pbl.star.dtos.request.post.CreatePostParams;
import com.pbl.star.usecase.PostInteractUsecase;
import com.pbl.star.usecase.PostManageUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostManageUsecase postManageUsecase;
    private final PostInteractUsecase postInteractUsecase;

    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> createPost(@RequestBody CreatePostParams createPostParams) {
        String postId = postManageUsecase.createPost(createPostParams);
        return ResponseEntity.ok(Map.of("id", postId));
    }

    @PostMapping("/{postId}/likes")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> likePost(@PathVariable String postId) {
        postInteractUsecase.likePost(postId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{postId}/likes")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> unlikePost(@PathVariable String postId) {
        postInteractUsecase.unlikePost(postId);
        return ResponseEntity.ok().build();
    }
}

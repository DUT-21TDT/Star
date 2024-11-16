package com.pbl.star.controllers.enduser;

import com.pbl.star.dtos.request.post.CreatePostParams;
import com.pbl.star.usecase.enduser.InteractPostUsecase;
import com.pbl.star.usecase.enduser.ManagePostUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final ManagePostUsecase postManageUsecase;
    private final InteractPostUsecase postInteractUsecase;

    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> createPost(@RequestBody CreatePostParams createPostParams) {

        String newPostId;

        if (createPostParams.getParentPostId() == null) {
            newPostId = postManageUsecase.createPost(createPostParams);
        }

        else {
            newPostId = postInteractUsecase.replyPost(createPostParams);
        }

        return ResponseEntity.ok(Map.of("id", newPostId));

    }

    @DeleteMapping("/{postId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> deletePost(@PathVariable String postId) {
        postManageUsecase.deletePost(postId);
        return ResponseEntity.ok().build();
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

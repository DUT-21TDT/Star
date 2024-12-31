package com.pbl.star.controllers.moderator;

import com.pbl.star.dtos.request.post.RejectPostParams;
import com.pbl.star.usecase.moderator.ModeratePostUsecase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/moderator/posts")
public class ModPostController {
    private final ModeratePostUsecase postManageUsecase;

//    @PatchMapping("/{postId}/status")
//    @PreAuthorize("hasAuthority('USER')")
//    public ResponseEntity<?> moderatePost(@PathVariable String postId, @RequestBody @Valid RejectPostParams moderatePostParams) {
//
//        PostStatus newStatus = PostStatus.valueOf(moderatePostParams.getStatus());
//
//        if (newStatus == PostStatus.APPROVED) {
//            postManageUsecase.approvePost(postId);
//        } else if (newStatus == PostStatus.REJECTED) {
//            postManageUsecase.rejectPost(postId);
//        } else if (newStatus == PostStatus.PENDING) {
//            postManageUsecase.returnPostToPending(postId);
//        }
//
//        return ResponseEntity.ok().build();
//    }

    @PatchMapping("/{postId}/approve")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> approvePost(@PathVariable String postId) {
        postManageUsecase.approvePost(postId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{postId}/pend")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> pendPost(@PathVariable String postId) {
        postManageUsecase.returnPostToPending(postId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{postId}/reject")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> rejectPost(@PathVariable String postId,
                                        @RequestBody @Valid RejectPostParams rejectPostParams
    ) {
        postManageUsecase.rejectPost(postId, rejectPostParams);
        return ResponseEntity.ok().build();
    }
}

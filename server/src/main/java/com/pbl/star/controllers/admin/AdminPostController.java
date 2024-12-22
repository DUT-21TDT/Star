package com.pbl.star.controllers.admin;

import com.pbl.star.dtos.request.post.FilterPostParams;
import com.pbl.star.usecase.admin.AdminManagePostUsecase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/posts")
@RequiredArgsConstructor
public class AdminPostController {

    private final AdminManagePostUsecase adminManagePostUsecase;
    @GetMapping
    public ResponseEntity<?> getAllPosts(@RequestParam(defaultValue = "0") @Min(0) int page,
                                         @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
                                         @ModelAttribute @Valid FilterPostParams filter)
    {
        return ResponseEntity.ok(adminManagePostUsecase.getAllPosts(page, size, filter));
    }

    @PatchMapping("/{postId}/hide")
    public ResponseEntity<?> hidePost(@PathVariable String postId) {
        adminManagePostUsecase.hidePost(postId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{postId}/unhide")
    public ResponseEntity<?> unhidePost(@PathVariable String postId) {
        adminManagePostUsecase.unhidePost(postId);
        return ResponseEntity.ok().build();
    }
}

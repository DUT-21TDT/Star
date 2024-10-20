package com.pbl.star.controllers;

import com.pbl.star.dtos.request.image.PostPresignedUrlsParams;
import com.pbl.star.usecase.ImageUploadUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {
    private final ImageUploadUsecase imageUploadUsecase;

    @PostMapping("/avatar-presigned-url")
    public ResponseEntity<?> createAvatarPresignedUrl(@RequestBody String fileName) {
        return ResponseEntity.ok(imageUploadUsecase.avatarPresignedUrl(fileName));
    }

    @PostMapping("/post-presigned-urls")
    public ResponseEntity<?> createPostPresignedUrls(@RequestBody PostPresignedUrlsParams params) {
        return ResponseEntity.ok(imageUploadUsecase.postPresignedUrls(params.getFileNames()));
    }
}

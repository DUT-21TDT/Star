package com.pbl.star.controllers;

import com.pbl.star.dtos.request.image.GetPresignedUrlsParams;
import com.pbl.star.usecase.ImageUploadUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {
    private final ImageUploadUsecase imageUploadUsecase;

    @PostMapping("/presigned-url")
    public ResponseEntity<?> createPresignedUrl(@RequestBody GetPresignedUrlsParams params) {
        return ResponseEntity.ok(imageUploadUsecase.generatePresignedUrls(params.getFileNames()));
    }
}

package com.pbl.star.controllers;

import com.pbl.star.usecase.ImageUploadUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {
    private final ImageUploadUsecase imageUploadUsecase;

    @PostMapping("/presigned-url")
    public ResponseEntity<?> createPresignedUrl(@RequestBody String fileName) {
        return ResponseEntity.ok(imageUploadUsecase.generatePresignedUrl(fileName));
    }
}

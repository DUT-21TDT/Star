package com.pbl.star.usecase.impl;

import com.pbl.star.services.S3Service;
import com.pbl.star.usecase.ImageUploadUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ImageUploadUsecaseImpl implements ImageUploadUsecase {
    private final S3Service s3Service;

    @Override
    public String generatePresignedUrl(String fileName) {
        return s3Service.generatePresignedUrl(fileName);
    }
}

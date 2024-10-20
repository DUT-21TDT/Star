package com.pbl.star.usecase.impl;

import com.pbl.star.dtos.response.image.PostPresignedUrlsResponse;
import com.pbl.star.exceptions.IllegalRequestArgumentException;
import com.pbl.star.services.external.S3Service;
import com.pbl.star.usecase.ImageUploadUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ImageUploadUsecaseImpl implements ImageUploadUsecase {
    private final S3Service s3Service;

    @Override
    public String avatarPresignedUrl(String fileName) {
        return s3Service.generatePresignedUrl(fileName);
    }

    @Override
    public PostPresignedUrlsResponse postPresignedUrls(List<String> fileNames) {

        if (fileNames == null || fileNames.isEmpty()) {
            return PostPresignedUrlsResponse.builder()
                    .successUrls(Set.of())
                    .missingFiles(Set.of())
                    .build();
        }

        if (fileNames.size() > 10) {
            throw new IllegalRequestArgumentException("Cannot generate presigned URLs for more than 10 files at once");
        }

        Set<String> objectKeys = Set.copyOf(fileNames);

        if (objectKeys.size() < fileNames.size()) {
            throw new IllegalRequestArgumentException("Duplicate file names are not allowed");
        }

        return s3Service.generatePresignedUrls(objectKeys);
    }
}

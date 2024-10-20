package com.pbl.star.usecase;

import com.pbl.star.dtos.response.image.GetPresignedUrlsResponse;

import java.util.List;

public interface ImageUploadUsecase {
    GetPresignedUrlsResponse generatePresignedUrls(List<String> fileNames);
}

package com.pbl.star.usecase.common;

import com.pbl.star.dtos.response.image.PostPresignedUrlsResponse;

import java.util.List;

public interface ImageUploadUsecase {
    String avatarPresignedUrl(String fileName);
    PostPresignedUrlsResponse postPresignedUrls(List<String> fileNames);
}

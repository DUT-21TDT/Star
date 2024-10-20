package com.pbl.star.services.external;

import com.pbl.star.dtos.response.image.GetPresignedUrlsResponse;

import java.util.Set;

public interface S3Service {
    GetPresignedUrlsResponse generatePresignedUrls(Set<String> objectKeys);
}

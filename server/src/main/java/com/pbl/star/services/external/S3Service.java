package com.pbl.star.services.external;

import com.pbl.star.dtos.response.image.PostPresignedUrlsResponse;
import org.springframework.lang.NonNull;

import java.util.Set;

public interface S3Service {
    PostPresignedUrlsResponse generatePresignedUrls(@NonNull Set<String> objectKeys);
    String generatePresignedUrl(@NonNull String objectKey);
}

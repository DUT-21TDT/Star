package com.pbl.star.services.external;

public interface S3Service {
    String generatePresignedUrl(String fileName);
}

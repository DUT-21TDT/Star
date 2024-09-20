package com.pbl.star.services;

public interface S3Service {
    String generatePresignedUrl(String fileName);
}

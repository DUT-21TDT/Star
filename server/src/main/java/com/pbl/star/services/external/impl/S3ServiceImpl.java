package com.pbl.star.services.external.impl;

import com.pbl.star.services.external.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {
    private final S3Presigner presigner;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Override
    public String generatePresignedUrl(String objectKey) {
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .putObjectRequest(objectRequest)
                .signatureDuration(Duration.ofMinutes(15))
                .build();

        return presigner.presignPutObject(presignRequest).url().toString();
    }
}

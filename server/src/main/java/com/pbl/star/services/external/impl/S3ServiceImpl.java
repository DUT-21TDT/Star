package com.pbl.star.services.external.impl;

import com.pbl.star.dtos.response.image.GetPresignedUrlsResponse;
import com.pbl.star.services.external.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {
    private final S3Presigner presigner;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Override
    public GetPresignedUrlsResponse generatePresignedUrls(Set<String> objectKeys) {

        Set<String> failedKeys = new HashSet<>(); // Create a list to store failed keys

        Set<CompletableFuture<String>> futures = objectKeys.stream()
                .map(objectKey -> CompletableFuture.supplyAsync(() -> generatePresignedUrlForKey(objectKey))
                        .handle((result, ex) -> {
                            if (ex != null) {
                                failedKeys.add(objectKey);
                                return null;
                            }
                            return result;
                        }))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // Wait for all tasks to complete and collect the results
        Set<String> successUrls = futures.stream()
                .map(CompletableFuture::join) // Collect results
                .collect(Collectors.toSet());

        return GetPresignedUrlsResponse.builder()
                .successUrls(successUrls)
                .missingFiles(failedKeys)
                .build();
    }

    private String generatePresignedUrlForKey(String objectKey) {
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

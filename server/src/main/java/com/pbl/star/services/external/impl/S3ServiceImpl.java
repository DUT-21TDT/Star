package com.pbl.star.services.external.impl;

import com.pbl.star.dtos.response.image.PostPresignedUrlsResponse;
import com.pbl.star.services.external.S3Service;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
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

    Logger logger = LoggerFactory.getLogger(S3ServiceImpl.class);


    private final S3Presigner presigner;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Override
    public PostPresignedUrlsResponse generatePresignedUrls(@NonNull Set<String> objectKeys) {

        if (objectKeys.size() == 1) {
            return generatePresignedUrlForOne(objectKeys.iterator().next());
        }

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
                .collect(Collectors.toSet());

        // Wait for all tasks to complete and collect the results
        Set<String> successUrls = futures.stream()
                .map(CompletableFuture::join) // Collect results
                .filter(Objects::nonNull) // Filter out null results
                .collect(Collectors.toSet());

        return PostPresignedUrlsResponse.builder()
                .successUrls(successUrls)
                .missingFiles(failedKeys)
                .build();
    }

    private PostPresignedUrlsResponse generatePresignedUrlForOne(@NonNull String objectKey) {
        try {
            return PostPresignedUrlsResponse.builder()
                    .successUrls(Set.of(generatePresignedUrlForKey(objectKey)))
                    .missingFiles(Set.of())
                    .build();
        } catch (Exception e) {
            return PostPresignedUrlsResponse.builder()
                    .successUrls(Set.of())
                    .missingFiles(Set.of(objectKey))
                    .build();
        }
    }

    public String generatePresignedUrl(@NonNull String objectKey) {
        try {
            return generatePresignedUrlForKey(objectKey);
        } catch (Exception e) {
            return null;
        }
    }

    private String generatePresignedUrlForKey(@NonNull String objectKey) {
        try {
            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build();

            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .putObjectRequest(objectRequest)
                    .signatureDuration(Duration.ofMinutes(15))
                    .build();

            return presigner.presignPutObject(presignRequest).url().toString();
        } catch (Exception e) {
            logger.error("Failed to generate presigned URL for object key: " + objectKey, e);
            throw e;
        }
    }
}

package com.pbl.star.dtos.response.image;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class PostPresignedUrlsResponse {
    private Set<String> successUrls;
    private Set<String> missingFiles;
}

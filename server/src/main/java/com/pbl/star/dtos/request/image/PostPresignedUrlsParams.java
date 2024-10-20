package com.pbl.star.dtos.request.image;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostPresignedUrlsParams {
    private List<String> fileNames;
}

package com.pbl.star.dtos.request.image;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostPresignedUrlsParams {
    @NotNull(message = "fileNames cannot be null")
    @Size(min = 1, max = 20, message = "fileNames must have at least 1 element")
    private List<String> fileNames;
}

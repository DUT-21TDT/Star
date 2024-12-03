package com.pbl.star.dtos.request.post;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreatePostParams {
    @NotNull(message = "Room ID is required")
    private String roomId;

    private String parentPostId;

    @Size(min = 1, max = 3000, message = "Content length must be between 1 and 3000")
    @NotNull(message = "Content is required")
    private String content;

    @Size(max = 20, message = "Image count must be less than or equal to 20")
    private List<String> imageFileNames;
}

package com.pbl.star.dtos.request.post;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreatePostParams {
    private String roomId;

    private String parentPostId;

    @Size(max = 3000, message = "Content length must be less than or equal to 3000")
    private String content;

    @Size(max = 20, message = "Image count must be less than or equal to 20")
    private List<String> imageFileNames;
}

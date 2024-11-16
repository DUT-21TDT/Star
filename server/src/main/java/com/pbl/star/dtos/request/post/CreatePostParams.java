package com.pbl.star.dtos.request.post;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreatePostParams {
    private String roomId;
    private String parentPostId;
    private String content;
    private List<String> imageFileNames;
}

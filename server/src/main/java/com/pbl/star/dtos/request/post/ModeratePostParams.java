package com.pbl.star.dtos.request.post;

import com.pbl.star.enums.PostStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModeratePostParams {
    private PostStatus status;
}

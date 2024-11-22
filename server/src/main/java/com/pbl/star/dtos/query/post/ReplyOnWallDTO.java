package com.pbl.star.dtos.query.post;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReplyOnWallDTO {
    private PostForUserDTO parentPost;
    private PostForUserDTO reply;
}

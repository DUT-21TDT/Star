package com.pbl.star.dtos.response.post;

import com.pbl.star.models.projections.post.PostForUser;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReplyOnWallResponse {
    private PostForUser parentPost;
    private PostForUser reply;
}

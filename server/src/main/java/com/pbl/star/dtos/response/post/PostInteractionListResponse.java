package com.pbl.star.dtos.response.post;

import com.pbl.star.models.projections.user.OnInteractProfile;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Slice;

@Getter
@Setter
@Builder
public class PostInteractionListResponse {
    private Slice<OnInteractProfile> actors;

    private Long viewsCount;
    private Long likesCount;
    private Long repostsCount;
}

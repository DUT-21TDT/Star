package com.pbl.star.models.projections.post;

import com.pbl.star.enums.PostStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class PendingPostForUser extends PostGeneral {
    private PostStatus status = PostStatus.PENDING;
}

package com.pbl.star.dtos.query.post;

import com.pbl.star.enums.PostStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class PendingPostForUserDTO extends PostGeneral {
    private PostStatus status = PostStatus.PENDING;
}

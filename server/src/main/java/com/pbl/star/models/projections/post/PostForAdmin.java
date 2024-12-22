package com.pbl.star.models.projections.post;

import com.pbl.star.enums.PostStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Getter
@Setter
@SuperBuilder
public class PostForAdmin extends PostGeneral {
    private PostStatus status;
    private Integer violenceScore;
    private String idOfModerator;
    private String usernameOfModerator;
    private Instant moderatedAt;

    private boolean isHidden;
    private Instant hideAt;
}

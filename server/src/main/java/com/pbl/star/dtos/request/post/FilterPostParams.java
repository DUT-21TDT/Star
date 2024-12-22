package com.pbl.star.dtos.request.post;

import com.pbl.star.enums.PostStatus;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class FilterPostParams {
    // By post id
    private String postId;
    // By type
    @Pattern(regexp = "^(?i)(POST|REPLY)$")
    private String type;
    // By room
    private String roomId;
    // By author
    private String username;
    // By content
    @Size(max = 100)
    private String content;
    // By mod status
    private PostStatus status;
    // By visibility
    private Boolean isHidden;
    // By time
    private Instant after;
}

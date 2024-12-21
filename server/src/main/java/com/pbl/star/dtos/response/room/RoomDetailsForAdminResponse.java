package com.pbl.star.dtos.response.room;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Builder
public class RoomDetailsForAdminResponse {
    private String id;
    private String name;
    private String description;
    private Instant createdAt;
    private Integer participantsCount;

    private Integer postsCount;
}

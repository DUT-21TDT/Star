package com.pbl.star.dtos.response.room;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Builder
public class RoomForAdminResponse {
    protected String id;
    protected String name;
    protected String description;
    protected Instant createdAt;
    protected Integer participantsCount;
}

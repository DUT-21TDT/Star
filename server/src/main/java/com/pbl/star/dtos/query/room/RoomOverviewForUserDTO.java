package com.pbl.star.dtos.query.room;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Builder
public class RoomOverviewForUserDTO {
    private String id;
    private String name;
    private String description;
    private Instant createdAt;
    private Integer participantsCount;
    @JsonProperty("isParticipant")
    private boolean isParticipant;
}

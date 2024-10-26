package com.pbl.star.dtos.query.room;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public final class RoomForUserDTO extends RoomGeneral {
    @JsonProperty("isParticipant")
    private boolean isParticipant;
}

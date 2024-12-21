package com.pbl.star.models.projections.room;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public final class RoomForUser extends RoomGeneral {
    private boolean isParticipant;
    private boolean isModerator;
}

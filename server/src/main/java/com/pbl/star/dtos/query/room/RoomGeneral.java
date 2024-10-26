package com.pbl.star.dtos.query.room;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Getter
@Setter
@SuperBuilder
abstract class RoomGeneral {
    protected String id;
    protected String name;
    protected String description;
    protected Instant createdAt;
    protected Integer participantsCount;
}

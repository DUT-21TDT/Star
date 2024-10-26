package com.pbl.star.dtos.query.room;

import lombok.experimental.SuperBuilder;

import java.time.Instant;

@SuperBuilder
abstract class RoomGeneral {
    protected String id;
    protected String name;
    protected String description;
    protected Instant createdAt;
    protected Integer participantsCount;
}

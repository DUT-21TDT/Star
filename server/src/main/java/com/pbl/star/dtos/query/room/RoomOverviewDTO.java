package com.pbl.star.dtos.query.room;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomOverviewDTO {
    private String id;
    private String name;
    private String description;
    private Instant createdAt;
    private Integer participantsCount;
}

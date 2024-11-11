package com.pbl.star.dtos.query.room;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class RoomDetailsForAdminDTO extends RoomGeneral {
    private Integer postsCount;
}

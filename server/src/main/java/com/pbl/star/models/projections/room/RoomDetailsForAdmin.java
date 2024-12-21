package com.pbl.star.models.projections.room;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class RoomDetailsForAdmin extends RoomGeneral {
    private Integer postsCount;
}

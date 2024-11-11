package com.pbl.star.dtos.query.room;

import com.pbl.star.dtos.query.user.ModInRoom;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
public class RoomDetailsForAdminDTO extends RoomGeneral {
    private Integer postsCount;
    private List<ModInRoom> moderators;
}

package com.pbl.star.repositories.extensions;

import com.pbl.star.dtos.query.room.RoomOverviewDTO;

import java.util.List;

public interface RoomRepositoryExtension {
    List<RoomOverviewDTO> getRoomsOverview();
}

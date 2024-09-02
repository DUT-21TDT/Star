package com.pbl.star.usecase.impl;

import com.pbl.star.dtos.request.room.CreateRoomParams;
import com.pbl.star.services.RoomService;
import com.pbl.star.usecase.RoomUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoomUsecaseImpl implements RoomUsecase {

    private final RoomService roomService;
    @Override
    public String createRoom(CreateRoomParams params) {
        return roomService.createRoom(params);
    }
}
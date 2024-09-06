package com.pbl.star.controllers;

import com.pbl.star.dtos.request.room.CreateRoomParams;
import com.pbl.star.usecase.RoomUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/room")
public class RoomController {

    private final RoomUsecase roomUsecase;

    @GetMapping
    public ResponseEntity<?> getAllRooms() {
        return ResponseEntity.ok(roomUsecase.getAllRooms());
    }

    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody @ModelAttribute CreateRoomParams params) {
        String roomId = roomUsecase.createRoom(params);
        return ResponseEntity.ok(Map.of("id", roomId));
    }
}

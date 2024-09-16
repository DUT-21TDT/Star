package com.pbl.star.controllers;

import com.pbl.star.dtos.request.room.CreateRoomParams;
import com.pbl.star.usecase.RoomUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController {

    private final RoomUsecase roomUsecase;

    @GetMapping
    public ResponseEntity<?> getAllRooms() {
        return ResponseEntity.ok(roomUsecase.getAllRooms());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> createRoom(@ModelAttribute CreateRoomParams params) {
        String roomId = roomUsecase.createRoom(params);
        return ResponseEntity.ok(Map.of("id", roomId));
    }

    @DeleteMapping("/{roomId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteRoom(@PathVariable String roomId) {
        roomUsecase.deleteRoom(roomId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{roomId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> updateRoom(@PathVariable String roomId, @ModelAttribute CreateRoomParams params) {
        roomUsecase.updateRoom(roomId, params);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{roomId}/members")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> joinRoom(@PathVariable String roomId) {
        roomUsecase.joinRoom(roomId);
        return ResponseEntity.ok().build();
    }
}

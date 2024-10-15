package com.pbl.star.controllers;

import com.pbl.star.dtos.request.room.CreateRoomParams;
import com.pbl.star.usecase.RoomInteractUsecase;
import com.pbl.star.usecase.RoomManageUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController {

    private final RoomManageUsecase roomManageUsecase;
    private final RoomInteractUsecase roomInteractUsecase;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getAllRooms() {
        return ResponseEntity.ok(roomManageUsecase.getAllRooms());
    }

    @GetMapping("/user-rooms")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> getUserRooms() {
        return ResponseEntity.ok(roomManageUsecase.getAllRoomsForUser());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> createRoom(@ModelAttribute CreateRoomParams params) {
        String roomId = roomManageUsecase.createRoom(params);
        return ResponseEntity.ok(Map.of("id", roomId));
    }

    @DeleteMapping("/{roomId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteRoom(@PathVariable String roomId) {
        roomManageUsecase.deleteRoom(roomId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{roomId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> updateRoom(@PathVariable String roomId, @ModelAttribute CreateRoomParams params) {
        roomManageUsecase.updateRoom(roomId, params);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{roomId}/members")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> joinRoom(@PathVariable String roomId) {
        roomInteractUsecase.joinRoom(roomId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{roomId}/members")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> leaveRoom(@PathVariable String roomId) {
        roomInteractUsecase.leaveRoom(roomId);
        return ResponseEntity.ok().build();
    }
}

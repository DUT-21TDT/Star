package com.pbl.star.controllers.admin;

import com.pbl.star.dtos.request.room.CreateRoomParams;
import com.pbl.star.usecase.admin.AdminManageRoomUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/rooms")
public class AdminRoomController {

    private final AdminManageRoomUsecase roomManageUsecase;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getAllRooms() {
        return ResponseEntity.ok(roomManageUsecase.getAllRooms());
    }

    @GetMapping("/{roomId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getRoomDetails(@PathVariable String roomId) {
        return ResponseEntity.ok(roomManageUsecase.getRoomDetails(roomId));
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
}


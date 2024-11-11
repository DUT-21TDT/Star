package com.pbl.star.controllers.admin;

import com.pbl.star.dtos.request.room.CreateRoomParams;
import com.pbl.star.usecase.RoomManageUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/rooms")
public class AdminRoomController {

    private final RoomManageUsecase roomManageUsecase;

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

    @GetMapping("/{roomId}/moderators")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getModerators(@PathVariable String roomId) {
        return ResponseEntity.ok(roomManageUsecase.getModerators(roomId));
    }

    @PostMapping("/{roomId}/moderators")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> addModerator(@PathVariable String roomId, @RequestBody Map<String, String> requestBody) {
        String userId = requestBody.get("userId");
        String username = requestBody.get("username");

        if (userId != null) {
            roomManageUsecase.addModeratorById(roomId, userId);
            return ResponseEntity.ok().build();
        } else if (username != null) {
            roomManageUsecase.addModeratorByUsername(roomId, username);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body("userId or username must be provided");
        }
    }

    @DeleteMapping("/{roomId}/moderators/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> removeModerator(@PathVariable String roomId, @PathVariable String userId) {
        roomManageUsecase.removeModerator(roomId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("{roomId}/members")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getMembers(@PathVariable String roomId, @RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(roomManageUsecase.getMembers(roomId, keyword));
    }
}


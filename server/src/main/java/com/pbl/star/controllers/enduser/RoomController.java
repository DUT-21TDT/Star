package com.pbl.star.controllers.enduser;

import com.pbl.star.usecase.RoomInteractUsecase;
import com.pbl.star.usecase.RoomManageUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController {

    private final RoomManageUsecase roomManageUsecase;
    private final RoomInteractUsecase roomInteractUsecase;

    @GetMapping
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> getUserRooms() {
        return ResponseEntity.ok(roomManageUsecase.getAllRoomsForUser());
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

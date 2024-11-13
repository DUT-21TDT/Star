package com.pbl.star.controllers.enduser;

import com.pbl.star.usecase.enduser.InteractRoomUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController {

    private final InteractRoomUsecase interactRoomUsecase;

    @GetMapping
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> getUserRooms() {
        return ResponseEntity.ok(interactRoomUsecase.getAllRoomsForUser());
    }

    @PostMapping("/{roomId}/members")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> joinRoom(@PathVariable String roomId) {
        interactRoomUsecase.joinRoom(roomId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{roomId}/members")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> leaveRoom(@PathVariable String roomId) {
        interactRoomUsecase.leaveRoom(roomId);
        return ResponseEntity.ok().build();
    }
}

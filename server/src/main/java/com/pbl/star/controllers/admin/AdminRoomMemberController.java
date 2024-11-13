package com.pbl.star.controllers.admin;

import com.pbl.star.usecase.admin.AdminManageRoomMemberUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/rooms/{roomId}")
public class AdminRoomMemberController {

    private final AdminManageRoomMemberUsecase roomMemberManageUsecase;

    @GetMapping("/moderators")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getModerators(@PathVariable String roomId) {
        return ResponseEntity.ok(roomMemberManageUsecase.getModerators(roomId));
    }

    @PostMapping("/moderators")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> addModerator(@PathVariable String roomId, @RequestBody Map<String, String> requestBody) {
        String userId = requestBody.get("userId");
        String username = requestBody.get("username");

        if (userId != null) {
            roomMemberManageUsecase.addModeratorById(roomId, userId);
            return ResponseEntity.ok().build();
        } else if (username != null) {
            roomMemberManageUsecase.addModeratorByUsername(roomId, username);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body("userId or username must be provided");
        }
    }

    @DeleteMapping("/moderators/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> removeModerator(@PathVariable String roomId, @PathVariable String userId) {
        roomMemberManageUsecase.removeModerator(roomId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/members")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getMembers(@PathVariable String roomId, @RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(roomMemberManageUsecase.getMembers(roomId, keyword));
    }
}


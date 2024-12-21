package com.pbl.star.controllers.admin;

import com.pbl.star.dtos.request.moderator.AddModeratorParams;
import com.pbl.star.usecase.admin.AdminManageRoomMemberUsecase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> addModerator(@PathVariable String roomId,
                                          @RequestBody @Valid AddModeratorParams params
    ) {
        roomMemberManageUsecase.addModeratorById(roomId, params.getUserId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/moderators/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> removeModerator(@PathVariable String roomId,
                                             @PathVariable String userId
    ) {
        roomMemberManageUsecase.removeModerator(roomId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/members")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getMembers(@PathVariable String roomId,
                                        @RequestParam(required = false) @Size(max = 50) String keyword
    ) {
        return ResponseEntity.ok(roomMemberManageUsecase.getMembers(roomId, keyword));
    }
}


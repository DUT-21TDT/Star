package com.pbl.star.controllers.admin;

import com.pbl.star.dtos.request.user.AdminGetUsersParams;
import com.pbl.star.usecase.admin.AdminManageUserUsecase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminManageUserUsecase manageUserUsecase;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getAllUsers(@ModelAttribute @Valid AdminGetUsersParams params) {
        return ResponseEntity.ok(manageUserUsecase.getAllUsers(params));
    }

    @PatchMapping("/{userId}/block")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> blockUser(@PathVariable String userId) {
        manageUserUsecase.blockUser(userId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{userId}/unblock")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> unblockUser(@PathVariable String userId) {
        manageUserUsecase.unblockUser(userId);
        return ResponseEntity.ok().build();
    }
}

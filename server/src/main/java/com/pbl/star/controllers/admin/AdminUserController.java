package com.pbl.star.controllers.admin;

import com.pbl.star.dtos.request.user.AdminGetUsersParams;
import com.pbl.star.usecase.admin.AdminManageUserUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Validated
public class AdminUserController {

    private final AdminManageUserUsecase manageUserUsecase;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getAllUsers(@ModelAttribute AdminGetUsersParams params) {
        return ResponseEntity.ok(manageUserUsecase.getAllUsers(params));
    }

    @PatchMapping("/{userId}/status")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> changeUserStatus(@PathVariable String userId, @RequestBody Map<String, String> requestBody) {

        String status = requestBody.get("status");

        if (status == null) {
            return ResponseEntity.badRequest().body("Status is required");
        }

        if (status.equalsIgnoreCase("block")) {
            manageUserUsecase.blockUser(userId);
        } else if (status.equalsIgnoreCase("unblock")) {
            manageUserUsecase.unblockUser(userId);
        } else {
            return ResponseEntity.badRequest().body("Invalid status");
        }
        return ResponseEntity.ok().build();
    }
}

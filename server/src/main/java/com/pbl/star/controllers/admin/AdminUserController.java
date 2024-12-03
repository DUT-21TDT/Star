package com.pbl.star.controllers.admin;

import com.pbl.star.dtos.request.user.AdminGetUsersParams;
import com.pbl.star.usecase.admin.AdminManageUserUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
}

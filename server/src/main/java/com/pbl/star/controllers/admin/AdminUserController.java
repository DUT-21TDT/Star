package com.pbl.star.controllers.admin;

import com.pbl.star.dtos.request.user.AdminGetUsersParams;
import com.pbl.star.usecase.admin.AdminManageUserUsecase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminManageUserUsecase manageUserUsecase;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getAllUsers(@ModelAttribute @Valid AdminGetUsersParams params,
                                         BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        return ResponseEntity.ok(manageUserUsecase.getAllUsers(params));
    }
}

package com.pbl.star.controllers.admin;

import com.pbl.star.dtos.request.user.AdminGetUsersParams;
import com.pbl.star.exceptions.IllegalRequestArgumentException;
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
    public ResponseEntity<?> getAllUsers(@Valid @ModelAttribute AdminGetUsersParams params,
                                         BindingResult result) {

        if (result.hasErrors()) {
             throw new IllegalRequestArgumentException(result.getAllErrors().getFirst().getDefaultMessage());
        }

        return ResponseEntity.ok(manageUserUsecase.getAllUsers(params));
    }
}

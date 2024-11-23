package com.pbl.star.usecase.admin.impl;

import com.pbl.star.dtos.query.user.OnDashboardProfileDTO;
import com.pbl.star.dtos.request.user.AdminGetUsersParams;
import com.pbl.star.services.domain.UserService;
import com.pbl.star.usecase.admin.AdminManageUserUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminManageUserUsecaseImpl implements AdminManageUserUsecase {

    private final UserService userService;

    @Override
    public Page<OnDashboardProfileDTO> getAllUsers(AdminGetUsersParams params) {
        return userService.getUsersAsAdmin(params);
    }
}

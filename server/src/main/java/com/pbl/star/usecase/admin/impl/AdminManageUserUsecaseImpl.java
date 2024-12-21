package com.pbl.star.usecase.admin.impl;

import com.pbl.star.dtos.response.user.OnDashboardProfileResponse;
import com.pbl.star.mapper.user.UserDTOMapper;
import com.pbl.star.models.projections.user.OnDashboardProfile;
import com.pbl.star.dtos.request.user.AdminGetUsersParams;
import com.pbl.star.enums.AccountStatus;
import com.pbl.star.services.domain.UserService;
import com.pbl.star.usecase.admin.AdminManageUserUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminManageUserUsecaseImpl implements AdminManageUserUsecase {

    private final UserService userService;

    private final UserDTOMapper mapper;

    @Override
    public Page<OnDashboardProfileResponse> getAllUsers(AdminGetUsersParams params) {
        return userService.getUsersAsAdmin(params).map(
                mapper::toDTO
        );
    }

    @Override
    public void blockUser(String userId) {
        userService.changeUserStatus(userId, AccountStatus.BLOCKED);
    }

    @Override
    public void unblockUser(String userId) {
        userService.changeUserStatus(userId, AccountStatus.ACTIVE);
    }
}

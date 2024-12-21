package com.pbl.star.usecase.admin;

import com.pbl.star.dtos.response.user.OnDashboardProfileResponse;
import com.pbl.star.dtos.request.user.AdminGetUsersParams;
import org.springframework.data.domain.Page;

public interface AdminManageUserUsecase {
    Page<OnDashboardProfileResponse> getAllUsers(AdminGetUsersParams params);
    void blockUser(String userId);
    void unblockUser(String userId);
}

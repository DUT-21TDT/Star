package com.pbl.star.usecase.admin;

import com.pbl.star.dtos.query.user.OnDashboardProfileDTO;
import com.pbl.star.dtos.request.user.AdminGetUsersParams;
import org.springframework.data.domain.Page;

public interface AdminManageUserUsecase {
    Page<OnDashboardProfileDTO> getAllUsers(AdminGetUsersParams params);
}

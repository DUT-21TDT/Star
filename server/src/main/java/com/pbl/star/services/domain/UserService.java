package com.pbl.star.services.domain;

import com.pbl.star.models.projections.user.BasicUserInfo;
import com.pbl.star.models.projections.user.OnDashboardProfile;
import com.pbl.star.models.projections.user.OnSearchProfile;
import com.pbl.star.models.projections.user.DetailsUserInfo;
import com.pbl.star.dtos.request.user.AdminGetUsersParams;
import com.pbl.star.dtos.request.user.ChangePasswordParams;
import com.pbl.star.dtos.request.user.UpdateProfileParams;
import com.pbl.star.models.projections.user.OnWallProfile;
import com.pbl.star.models.entities.User;
import com.pbl.star.enums.AccountStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

import java.util.Optional;

public interface UserService {
    Optional<BasicUserInfo> getGeneralInformation(String userId);
    Slice<OnSearchProfile> searchUsers(String currentUserId, String keyword, int limit, String afterId);
    OnWallProfile getProfileOnWall(String currentUserId, String targetUserId);
    DetailsUserInfo getPersonalInformation(String userId);
    User updatePersonalInformation(User user, UpdateProfileParams updateProfileParams);
    User getUserById(String userId);
    Page<OnDashboardProfile> getUsersAsAdmin(AdminGetUsersParams params);
    void changePassword(String userId, ChangePasswordParams changePasswordParams);
    void changeUserStatus(String userId, AccountStatus targetStatus);
}

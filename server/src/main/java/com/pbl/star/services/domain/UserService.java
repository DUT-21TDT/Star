package com.pbl.star.services.domain;

import com.pbl.star.dtos.query.user.GeneralInformation;
import com.pbl.star.dtos.query.user.OnDashboardProfileDTO;
import com.pbl.star.dtos.query.user.OnSearchProfile;
import com.pbl.star.dtos.query.user.PersonalInformation;
import com.pbl.star.dtos.request.user.AdminGetUsersParams;
import com.pbl.star.dtos.request.user.UpdateProfileParams;
import com.pbl.star.dtos.response.user.OnWallProfileResponse;
import com.pbl.star.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

import java.util.Optional;

public interface UserService {
    Optional<GeneralInformation> getGeneralInformation(String userId);
    Slice<OnSearchProfile> searchUsers(String currentUserId, String keyword, int limit, String afterId);
    OnWallProfileResponse getProfileOnWall(String currentUserId, String targetUserId);
    PersonalInformation getPersonalInformation(String userId);
    User updatePersonalInformation(User user, UpdateProfileParams updateProfileParams);
    User getUserById(String userId);
    Page<OnDashboardProfileDTO> getUsersAsAdmin(AdminGetUsersParams params);
}

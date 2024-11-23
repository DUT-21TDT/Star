package com.pbl.star.repositories.extensions;

import com.pbl.star.dtos.query.user.OnDashboardProfileDTO;
import com.pbl.star.dtos.query.user.OnSearchProfile;
import com.pbl.star.dtos.query.user.PersonalInformation;
import com.pbl.star.dtos.query.user.UserInRoom;
import com.pbl.star.dtos.response.user.OnWallProfileResponse;
import com.pbl.star.enums.AccountStatus;
import com.pbl.star.enums.SortDirection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface UserRepositoryExtension {
    Slice<OnSearchProfile> searchUsers(Pageable pageable, String afterId, String currentUserId, String keyword);
    OnWallProfileResponse getPublicProfile(String currentUserId, String targetUserId);
    PersonalInformation getPersonalInformation(String userId);
    List<UserInRoom> findUsersInRoom(String roomId, String keyword);
    Page<OnDashboardProfileDTO> findUsersAsAdmin(Pageable pageable,
                                                 String keyword,
                                                 AccountStatus status,
                                                 String sortBy,
                                                 SortDirection direction);
}

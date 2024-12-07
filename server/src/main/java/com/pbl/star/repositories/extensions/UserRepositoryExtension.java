package com.pbl.star.repositories.extensions;

import com.pbl.star.dtos.query.user.*;
import com.pbl.star.enums.AccountStatus;
import com.pbl.star.enums.SortDirection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserRepositoryExtension {
    List<OnSearchProfile> searchUsers(int limit, String afterId, String currentUserId, String keyword);
    OnWallProfile getPublicProfile(String currentUserId, String targetUserId);
    PersonalInformation getPersonalInformation(String userId);
    List<UserInRoom> findUsersInRoom(String roomId, String keyword);
    Page<OnDashboardProfileDTO> findUsersAsAdmin(Pageable pageable,
                                                 String keyword,
                                                 AccountStatus status,
                                                 String sortBy,
                                                 SortDirection direction);
}

package com.pbl.star.repositories.extensions;

import com.pbl.star.enums.AccountStatus;
import com.pbl.star.enums.SortDirection;
import com.pbl.star.models.projections.user.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserRepositoryExtension {
    List<OnSearchProfile> searchUsers(int limit, String afterId, String currentUserId, String keyword);
    OnWallProfile getPublicProfile(String currentUserId, String targetUserId);
    DetailsUserInfo getPersonalInformation(String userId);
    List<UserInRoom> findUsersInRoom(String roomId, String keyword);
    Page<OnDashboardProfile> findUsersAsAdmin(Pageable pageable,
                                              String keyword,
                                              AccountStatus status,
                                              String sortBy,
                                              SortDirection direction);
}

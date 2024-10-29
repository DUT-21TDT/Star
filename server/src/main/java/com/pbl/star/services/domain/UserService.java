package com.pbl.star.services.domain;

import com.pbl.star.dtos.query.user.GeneralInformation;
import com.pbl.star.dtos.query.user.PersonalInformation;
import com.pbl.star.dtos.request.user.UpdateProfileParams;
import com.pbl.star.dtos.response.user.FollowResponse;
import com.pbl.star.dtos.response.user.PublicProfileResponse;
import com.pbl.star.entities.User;
import com.pbl.star.enums.FollowRequestAction;

import java.util.Optional;

public interface UserService {
    Optional<GeneralInformation> getGeneralInformation(String userId);
    PublicProfileResponse getPublicProfile(String userId);
    PersonalInformation getPersonalInformation(String userId);
    User updatePersonalInformation(String userId, UpdateProfileParams updateProfileParams);
    FollowResponse sendFollowRequest(String followerId, String followeeId);
    void updateFollowRequestStatus(String userId, String followingId, FollowRequestAction action);
    void removeFollowRequest(String followerId, String followeeId);
}

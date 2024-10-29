package com.pbl.star.repositories.extensions;

import com.pbl.star.dtos.query.user.PersonalInformation;
import com.pbl.star.dtos.response.user.PublicProfileResponse;

public interface UserRepositoryExtension {
    PublicProfileResponse getPublicProfile(String currentUserId, String targetUserId);
    PersonalInformation getPersonalInformation(String userId);
}

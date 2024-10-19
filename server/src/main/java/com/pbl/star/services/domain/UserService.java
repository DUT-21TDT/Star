package com.pbl.star.services.domain;

import com.pbl.star.dtos.query.user.GeneralInformation;
import com.pbl.star.dtos.query.user.PersonalInformation;
import com.pbl.star.dtos.request.user.UpdateProfileParams;
import com.pbl.star.dtos.response.user.PublicProfileResponse;
import com.pbl.star.entities.User;

import java.util.Optional;

public interface UserService {
    Optional<GeneralInformation> getGeneralInformation(String userId);
    PublicProfileResponse getPublicProfile(String userId);
    PersonalInformation getPersonalInformation(String userId);
    User updatePersonalInformation(String userId, UpdateProfileParams updateProfileParams);
}

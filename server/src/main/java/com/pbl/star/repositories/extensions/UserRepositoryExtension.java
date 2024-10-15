package com.pbl.star.repositories.extensions;

import com.pbl.star.dtos.query.user.PersonalInformation;
import com.pbl.star.dtos.query.user.PublicProfile;

public interface UserRepositoryExtension {
    PublicProfile getPublicProfile(String username);
    PersonalInformation getPersonalInformation(String userId);
}

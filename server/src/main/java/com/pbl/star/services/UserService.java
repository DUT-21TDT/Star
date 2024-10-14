package com.pbl.star.services;

import com.pbl.star.dtos.query.user.GeneralInformation;
import com.pbl.star.dtos.query.user.PersonalInformation;
import com.pbl.star.dtos.request.user.UpdateProfileParams;
import com.pbl.star.dtos.response.user.PublicProfileResponse;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface UserService {
    GeneralInformation getGeneralInformation(String userId);
    PublicProfileResponse getPublicProfile(String userId);
    Collection<GrantedAuthority> getUserAuthorities(String userId);
    PersonalInformation getPersonalInformation(String userId);
    void updatePersonalInformation(UpdateProfileParams updateProfileParams);
    void removeInactiveAccountByEmail(String email);
}

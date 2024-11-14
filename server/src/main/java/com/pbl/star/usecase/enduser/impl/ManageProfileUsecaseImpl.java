package com.pbl.star.usecase.enduser.impl;

import com.pbl.star.dtos.query.user.GeneralInformation;
import com.pbl.star.dtos.query.user.PersonalInformation;
import com.pbl.star.dtos.request.user.UpdateProfileParams;
import com.pbl.star.entities.User;
import com.pbl.star.services.domain.FollowService;
import com.pbl.star.services.domain.UserService;
import com.pbl.star.usecase.enduser.ManageProfileUsecase;
import com.pbl.star.utils.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ManageProfileUsecaseImpl implements ManageProfileUsecase {

    private final UserService userService;
    private final FollowService followService;

    @Override
    public GeneralInformation getGeneralInformation() {
        return AuthUtil.getCurrentUser();
    }

    @Override
    public PersonalInformation getPersonalInformation() {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        return userService.getPersonalInformation(currentUserId);
    }

    @Override
    public GeneralInformation updatePersonalInformation(UpdateProfileParams updateProfileParams) {
        String currentUserId = AuthUtil.getCurrentUser().getId();

        User currentUser = userService.getUserById(currentUserId);
        boolean previousPrivateProfile = currentUser.isPrivateProfile();

        User updatedUser = userService.updatePersonalInformation(currentUser, updateProfileParams);

        // If change from private to public, accept all follow requests
        if (previousPrivateProfile && !updateProfileParams.isPrivateProfile()) {
            acceptAllFollowRequests();
        }

        return new GeneralInformation(updatedUser);
    }

    @Override
    public void acceptAllFollowRequests() {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        followService.acceptAllFollowRequests(currentUserId);
    }
}

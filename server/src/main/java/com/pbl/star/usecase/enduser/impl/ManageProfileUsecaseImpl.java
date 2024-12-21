package com.pbl.star.usecase.enduser.impl;

import com.pbl.star.dtos.response.user.BasicUserInfoResponse;
import com.pbl.star.dtos.response.user.DetailsUserInfoResponse;
import com.pbl.star.mapper.user.UserDTOMapper;
import com.pbl.star.models.projections.user.BasicUserInfo;
import com.pbl.star.dtos.request.user.ChangePasswordParams;
import com.pbl.star.dtos.request.user.UpdateProfileParams;
import com.pbl.star.models.entities.User;
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

    private final UserDTOMapper userMapper;
    @Override
    public BasicUserInfoResponse getGeneralInformation() {
        return userMapper.toDTO(AuthUtil.getCurrentUser());
    }

    @Override
    public DetailsUserInfoResponse getPersonalInformation() {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        return userMapper.toDTO(userService.getPersonalInformation(currentUserId));
    }

    @Override
    public BasicUserInfoResponse updatePersonalInformation(UpdateProfileParams updateProfileParams) {
        String currentUserId = AuthUtil.getCurrentUser().getId();

        User currentUser = userService.getUserById(currentUserId);
        boolean previousPrivateProfile = currentUser.isPrivateProfile();

        User updatedUser = userService.updatePersonalInformation(currentUser, updateProfileParams);

        // If change from private to public, accept all follow requests
        if (previousPrivateProfile && !updateProfileParams.isPrivateProfile()) {
            acceptAllFollowRequests();
        }

        return userMapper.toDTO(new BasicUserInfo(updatedUser));
    }

    private void acceptAllFollowRequests() {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        followService.acceptAllFollowRequests(currentUserId);
    }

    @Override
    public void changePassword(ChangePasswordParams changePasswordParams) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        userService.changePassword(currentUserId, changePasswordParams);
    }
}

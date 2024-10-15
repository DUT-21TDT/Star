package com.pbl.star.usecase.impl;

import com.pbl.star.dtos.query.user.GeneralInformation;
import com.pbl.star.dtos.query.user.PersonalInformation;
import com.pbl.star.dtos.request.user.UpdateProfileParams;
import com.pbl.star.services.UserService;
import com.pbl.star.usecase.ProfileManageUsecase;
import com.pbl.star.utils.AuthUtil;
import com.pbl.star.utils.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProfileManageUsecaseImpl implements ProfileManageUsecase {

    private final UserService userService;

    @Override
    public GeneralInformation getGeneralInformation() {
        String userId = AuthUtil.getCurrentUser().getId();
        return userService.getGeneralInformation(userId);
    }

    @Override
    public PersonalInformation getPersonalInformation() {
        CurrentUser currentUser = AuthUtil.getCurrentUser();
        return userService.getPersonalInformation(currentUser.getId());
    }

    @Override
    public void updatePersonalInformation(UpdateProfileParams updateProfileParams) {
        userService.updatePersonalInformation(updateProfileParams);
    }
}

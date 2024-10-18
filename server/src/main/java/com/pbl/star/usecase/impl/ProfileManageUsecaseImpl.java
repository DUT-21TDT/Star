package com.pbl.star.usecase.impl;

import com.pbl.star.dtos.query.user.GeneralInformation;
import com.pbl.star.dtos.query.user.PersonalInformation;
import com.pbl.star.dtos.request.user.UpdateProfileParams;
import com.pbl.star.services.domain.UserService;
import com.pbl.star.usecase.ProfileManageUsecase;
import com.pbl.star.utils.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProfileManageUsecaseImpl implements ProfileManageUsecase {

    private final UserService userService;

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
    public void updatePersonalInformation(UpdateProfileParams updateProfileParams) {
        userService.updatePersonalInformation(updateProfileParams);
    }
}

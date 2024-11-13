package com.pbl.star.usecase.enduser.impl;

import com.pbl.star.dtos.query.user.GeneralInformation;
import com.pbl.star.dtos.query.user.PersonalInformation;
import com.pbl.star.dtos.request.user.UpdateProfileParams;
import com.pbl.star.entities.User;
import com.pbl.star.services.domain.UserService;
import com.pbl.star.usecase.enduser.ManageProfileUsecase;
import com.pbl.star.utils.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ManageProfileUsecaseImpl implements ManageProfileUsecase {

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
    public GeneralInformation updatePersonalInformation(UpdateProfileParams updateProfileParams) {
        String currentUserId = AuthUtil.getCurrentUser().getId();
        User updatedUser = userService.updatePersonalInformation(currentUserId, updateProfileParams);
        return new GeneralInformation(updatedUser);
    }
}

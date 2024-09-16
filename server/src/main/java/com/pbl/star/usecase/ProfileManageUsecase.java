package com.pbl.star.usecase;

import com.pbl.star.dtos.query.user.PersonalInformation;
import com.pbl.star.dtos.request.user.UpdateProfileParams;

public interface ProfileManageUsecase {
    PersonalInformation getPersonalInformation();
    void updatePersonalInformation(UpdateProfileParams updateProfileParams);
}

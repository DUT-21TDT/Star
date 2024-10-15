package com.pbl.star.usecase;

import com.pbl.star.dtos.query.user.GeneralInformation;
import com.pbl.star.dtos.query.user.PersonalInformation;
import com.pbl.star.dtos.request.user.UpdateProfileParams;

public interface ProfileManageUsecase {
    GeneralInformation getGeneralInformation();
    PersonalInformation getPersonalInformation();
    void updatePersonalInformation(UpdateProfileParams updateProfileParams);
}

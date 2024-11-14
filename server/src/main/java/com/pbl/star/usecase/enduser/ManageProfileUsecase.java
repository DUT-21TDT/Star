package com.pbl.star.usecase.enduser;

import com.pbl.star.dtos.query.user.GeneralInformation;
import com.pbl.star.dtos.query.user.PersonalInformation;
import com.pbl.star.dtos.request.user.UpdateProfileParams;

public interface ManageProfileUsecase {
    GeneralInformation getGeneralInformation();
    PersonalInformation getPersonalInformation();
    GeneralInformation updatePersonalInformation(UpdateProfileParams updateProfileParams);
    void acceptAllFollowRequests();
}

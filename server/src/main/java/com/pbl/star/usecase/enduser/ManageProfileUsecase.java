package com.pbl.star.usecase.enduser;

import com.pbl.star.dtos.response.user.BasicUserInfoResponse;
import com.pbl.star.dtos.response.user.DetailsUserInfoResponse;
import com.pbl.star.dtos.request.user.ChangePasswordParams;
import com.pbl.star.dtos.request.user.UpdateProfileParams;

public interface ManageProfileUsecase {
    BasicUserInfoResponse getGeneralInformation();
    DetailsUserInfoResponse getPersonalInformation();
    BasicUserInfoResponse updatePersonalInformation(UpdateProfileParams updateProfileParams);
    void changePassword(ChangePasswordParams changePasswordParams);
}

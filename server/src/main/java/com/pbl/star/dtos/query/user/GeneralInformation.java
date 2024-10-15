package com.pbl.star.dtos.query.user;

import com.pbl.star.enums.AccountStatus;
import com.pbl.star.enums.UserRole;

public interface GeneralInformation {
    String getId();
    String getUsername();
    String getAvatarUrl();
    UserRole getRole();
    AccountStatus getStatus();

}

package com.pbl.star.dtos.response.user;

import com.pbl.star.enums.AccountStatus;
import com.pbl.star.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BasicUserInfoResponse {
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private String avatarUrl;
    private UserRole role;
    private AccountStatus status;
    private boolean hasPassword;
}

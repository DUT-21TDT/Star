package com.pbl.star.dtos.query.user;

import com.pbl.star.enums.AccountStatus;
import com.pbl.star.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GeneralInformation {
    private String id;
    private String username;
    private String avatarUrl;
    private UserRole role;
    private AccountStatus status;
}

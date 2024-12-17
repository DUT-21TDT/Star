package com.pbl.star.dtos.query.user;

import com.pbl.star.entities.User;
import com.pbl.star.enums.AccountStatus;
import com.pbl.star.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GeneralInformation {
    private String id;
    private String username;
    private String avatarUrl;
    private UserRole role;
    private AccountStatus status;
    private boolean hasPassword;

    public GeneralInformation(String id, String username, String avatarUrl, UserRole role, AccountStatus status, String password) {
        this.id = id;
        this.username = username;
        this.avatarUrl = avatarUrl;
        this.role = role;
        this.status = status;
        this.hasPassword = password != null;
    }

    public GeneralInformation(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.avatarUrl = user.getAvatarUrl();
        this.role = user.getRole();
        this.status = user.getStatus();
        this.hasPassword = user.getPassword() != null;
    }
}

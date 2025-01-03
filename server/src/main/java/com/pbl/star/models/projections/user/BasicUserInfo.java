package com.pbl.star.models.projections.user;

import com.pbl.star.enums.AccountStatus;
import com.pbl.star.enums.UserRole;
import com.pbl.star.models.entities.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BasicUserInfo {
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private String avatarUrl;
    private UserRole role;
    private AccountStatus status;
    private boolean hasPassword;

    public BasicUserInfo(String id, String username, String firstName, String lastName, String avatarUrl, UserRole role, AccountStatus status, String password) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatarUrl = avatarUrl;
        this.role = role;
        this.status = status;
        this.hasPassword = password != null;
    }

    public BasicUserInfo(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.avatarUrl = user.getAvatarUrl();
        this.role = user.getRole();
        this.status = user.getStatus();
        this.hasPassword = user.getPassword() != null;
    }
}

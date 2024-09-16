package com.pbl.star.dtos.request.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProfileParams {
    private String username;
    private String firstName;
    private String lastName;
    private String bio;
    private String avatarUrl;
    private String dateOfBirth;
    private String gender;
    private boolean privateProfile;
}

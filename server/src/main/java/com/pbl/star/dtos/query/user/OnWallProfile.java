package com.pbl.star.dtos.query.user;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

@Getter
@Setter
@Builder
public class OnWallProfile {
    private String username;
    private String firstName;
    private String lastName;
    private String bio;
    private String avatarUrl;
    private boolean privateProfile;
    private int numberOfFollowers;
}

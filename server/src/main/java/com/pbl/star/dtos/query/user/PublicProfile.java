package com.pbl.star.dtos.query.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PublicProfile {
    private String username;
    private String firstName;
    private String lastName;
    private String bio;
    private String avatarUrl;
    private boolean privateProfile;
    private int numberOfFollowers;
}

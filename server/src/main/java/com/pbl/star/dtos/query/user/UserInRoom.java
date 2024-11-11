package com.pbl.star.dtos.query.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserInRoom {
    private String userId;
    private String username;
    private String avatarUrl;
    private String firstName;
    private String lastName;
}

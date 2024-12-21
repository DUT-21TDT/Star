package com.pbl.star.dtos.response.user;

import com.pbl.star.enums.Gender;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Builder
public class OnDashboardProfileResponse {
    private String id;
    private String username;
    private String avatarUrl;
    private String firstName;
    private String lastName;
    private String email;
    private Gender gender;
    private Instant registerAt;
}

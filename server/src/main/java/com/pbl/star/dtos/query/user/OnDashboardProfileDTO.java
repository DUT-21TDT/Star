package com.pbl.star.dtos.query.user;

import com.pbl.star.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class OnDashboardProfileDTO {
    private String id;
    private String username;
    private String avatarUrl;
    private String firstName;
    private String lastName;
    private String email;
    private Gender gender;
    private Instant registerAt;
}

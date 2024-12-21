package com.pbl.star.dtos.response.user;

import com.pbl.star.enums.Gender;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class DetailsUserInfoResponse {
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private String bio;
    private String avatarUrl;
    private LocalDate dateOfBirth;
    private Gender gender;
    private Instant registerAt;
    private Boolean privateProfile;
}

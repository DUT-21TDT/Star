package com.pbl.star.dtos.query.user;

import com.pbl.star.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class PersonalInformation {
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

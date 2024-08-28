package com.pbl.star.dtos.request;

import com.pbl.star.enums.Gender;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpParams {
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    @Setter(AccessLevel.NONE)
    private Gender gender;

    public void setGender(String gender) {
        if (gender == null || gender.isBlank()) {
            this.gender = null;
        } else {
            this.gender = Gender.valueOf(gender);
        }
    }
}

package com.pbl.star.dtos.request.user;

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
}

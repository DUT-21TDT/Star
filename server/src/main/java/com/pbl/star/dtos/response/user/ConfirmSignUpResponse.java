package com.pbl.star.dtos.response.user;

import lombok.*;

@Getter
@Setter
@Builder
public class ConfirmSignUpResponse {
    private String id;
    private String username;
    private String email;
}

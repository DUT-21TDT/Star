package com.pbl.star.dtos.request.user;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResendEmailParams {
    @Email(message = "Invalid email address")
    private String email;
}

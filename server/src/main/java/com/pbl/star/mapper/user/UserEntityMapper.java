package com.pbl.star.mapper.user;

import com.pbl.star.dtos.request.user.SignUpParams;
import com.pbl.star.models.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public abstract class UserEntityMapper {

    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Mapping(target = "username", expression = "java(signUpParams.getUsername().toLowerCase())")
    @Mapping(target = "password", expression = "java(passwordEncoder.encode(signUpParams.getPassword()))")
    @Mapping(target = "role", constant = "USER")
    @Mapping(target = "status", constant = "INACTIVE")
    @Mapping(target = "privateProfile", constant = "false")
    public abstract User toEntity(SignUpParams signUpParams);
}

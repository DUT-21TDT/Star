package com.pbl.star.security;

import com.pbl.star.models.projections.user.BasicUserInfo;
import com.pbl.star.services.domain.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final UserService userService;
    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        // Extract user information from the JWT
        String userId = jwt.getClaimAsString("sub");

        BasicUserInfo user = userService.getGeneralInformation(userId).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );

        Collection<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(
                user.getRole().name()
        ));

        return new UsernamePasswordAuthenticationToken(user, null, authorities);
    }
}

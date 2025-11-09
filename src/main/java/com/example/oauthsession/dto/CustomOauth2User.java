package com.example.oauthsession.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class CustomOauth2User implements OAuth2User {

    private final OAuth2Response oAuth2Response;
    private final String role;

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){ //role값
        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(() -> role);
        return collection;
    }

    @Override
    public String getName() {
        return oAuth2Response.getName();
    }
}

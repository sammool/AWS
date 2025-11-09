package com.example.oauthsession.dto;

import java.util.HashMap;
import java.util.Map;

public class NaverResposne implements OAuth2Response{

    private final Map<String, Object> attribute;

    public NaverResposne(Map<String, Object> attribute){
        this.attribute = (Map<String, Object>) attribute.get("response");
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getProviderId() {
        return attribute.get("id").toString();
    }

    @Override
    public String getEmail() {
        return attribute.get("email").toString();
    }

    @Override
    public String getName() {
        return attribute.get("name").toString();
    }
}

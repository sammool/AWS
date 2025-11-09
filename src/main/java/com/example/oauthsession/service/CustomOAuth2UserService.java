package com.example.oauthsession.service;

import com.example.oauthsession.dto.NaverResposne;
import com.example.oauthsession.dto.OAuth2Response;
import com.example.oauthsession.entity.User;
import com.example.oauthsession.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User.getAttributes());

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        String role = null;

        if(registrationId.equals("naver")){
            oAuth2Response = new NaverResposne(oAuth2User.getAttributes());
        }

        String username = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
        User existData = userRepository.findByUsername(username);
        if(existData == null){
            User user = new User();
            user.setUsername(username);
            user.setEmail(oAuth2Response.getEmail());
            user.setRole("ROLE_USER");

            userRepository.save(user);
        }else{
            existData.setEmail(oAuth2Response.getEmail());
            userRepository.save(existData);
        }

        return oAuth2User;
    }
}

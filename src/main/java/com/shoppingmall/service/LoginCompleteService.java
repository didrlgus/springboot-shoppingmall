package com.shoppingmall.service;

import com.shoppingmall.domain.SocialUser;
import com.shoppingmall.dto.SocialUserResponseDto;
import com.shoppingmall.repository.SocialUserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.shoppingmall.domain.enums.SocialType.*;

@Service
@Slf4j
public class LoginCompleteService {

    @Autowired
    private SocialUserRepository socialUserRepository;
    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    public void loginProc(OAuth2AuthenticationToken authentication) {
        log.info("#### loginProc : " + authentication.getPrincipal());

        HttpSession session
                = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getSession();

        SocialUser user = (SocialUser) session.getAttribute("user");

        if (user != null)
            return;

        if (authentication.isAuthenticated()) {
            Map<String, Object> attributesMap = authentication.getPrincipal().getAttributes();

            SocialUser convertedUser = convertSocialUser(authentication, attributesMap);

            SocialUser socialUser = socialUserRepository.findByPrincipal(convertedUser.getPrincipal());

            if (socialUser == null) {
                socialUserRepository.save(convertedUser);
            } else {
                if (!socialUser.getAccessToken().equals(convertedUser.getAccessToken())) {
                    socialUser.setAccessToken(convertedUser.getAccessToken());
                    socialUserRepository.save(socialUser);
                }
            }

            log.info("### convertedUser : " + convertedUser);

            SocialUserResponseDto socialUserResponseDto = SocialUserResponseDto.builder()
                    .name(convertedUser.getName())
                    .email(convertedUser.getEmail())
                    .socialType(convertedUser.getSocialType())
                    .build();

            session.setAttribute("user", socialUserResponseDto);
        }
    }

    private SocialUser convertSocialUser(OAuth2AuthenticationToken authentication, Map<String, Object> attributesMap) {
        SocialUser socialUser = null;

        String authenticationClientRegistrationId = authentication.getAuthorizedClientRegistrationId();

        if (authenticationClientRegistrationId.equals("kakao")) {

            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(authentication.getPrincipal().getAttributes(), "N/A",
                    AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_USER")));

            List<String> authoritiesList
                    = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().map(String::valueOf).collect(Collectors.toList());


            HashMap<String, Object> propMap = (HashMap<String, Object>) attributesMap.get("properties");

            socialUser = SocialUser.builder()
                    .name(String.valueOf(propMap.get("nickname")))
                    .principal(String.valueOf(attributesMap.get("id")))
                    .socialType(KAKAO)
                    .accessToken(getAccessToken(authentication))
                    .authorities(authoritiesList.toString())
                    .createdDate(LocalDateTime.now())
                    .build();

        } else if (authenticationClientRegistrationId.equals("google")) {

            socialUser = SocialUser.builder()
                    .name(String.valueOf(attributesMap.get("name")))
                    .email(String.valueOf(attributesMap.get("email")))
                    .principal(String.valueOf(attributesMap.get("sub")))
                    .socialType(GOOGLE)
                    .accessToken(getAccessToken(authentication))
                    .authorities(String.valueOf(SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray()[0]))
                    .createdDate(LocalDateTime.now())
                    .build();
        } else if (authenticationClientRegistrationId.equals("github")) {

            socialUser = SocialUser.builder()
                    .name(String.valueOf(attributesMap.get("name")))
                    .principal(String.valueOf(attributesMap.get("id")))
                    .socialType(GITHUB)
                    .accessToken(getAccessToken(authentication))
                    .authorities(String.valueOf(SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray()[0]))
                    .createdDate(LocalDateTime.now())
                    .build();
        }

        return socialUser;
    }

    private String getAccessToken(OAuth2AuthenticationToken authentication) {

        OAuth2AuthorizedClient client = authorizedClientService
                .loadAuthorizedClient(
                        authentication.getAuthorizedClientRegistrationId(),
                        authentication.getName());

        return client.getAccessToken().getTokenValue();
    }
}

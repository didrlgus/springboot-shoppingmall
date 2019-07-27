package com.shoppingmall.service;

import com.shoppingmall.domain.NormalUser;
import com.shoppingmall.dto.NormalUserRequestDto;
import com.shoppingmall.repository.NormalUserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
@Service
public class UserService {

    private NormalUserRepository normalUserRepository;

    /*public void saveUser() {
        NormalUser user = NormalUser.builder()
                .identifier("didrlgus")
                .name("양기현")
                .email("rlgusdid@naver.com")
                .build();
        userRepository.save(user);
    }*/

    public void userRegistration(NormalUserRequestDto userDto) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userDto.setAuthorities("ROLE_USER");

        log.info("### normalUserRequestDto: {}", userDto.toString());

        NormalUser normalUser = NormalUser.builder()
                .identifier(userDto.getIdentifier())
                .name(userDto.getName())
                .password(userDto.getPassword())
                .email(userDto.getEmail())
                .authorities(userDto.getAuthorities())
                .build();

        normalUserRepository.save(normalUser);
    }
}

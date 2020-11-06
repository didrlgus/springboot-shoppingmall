package com.shoppingmall.service;

import com.shoppingmall.domain.enums.Role;
import com.shoppingmall.domain.user.User;
import com.shoppingmall.domain.user.UserRepository;
import com.shoppingmall.dto.*;
import com.shoppingmall.exception.DuplicatedException;
import com.shoppingmall.exception.NotExistUserException;
import com.shoppingmall.exception.UpdatePasswordException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 일반유저 회원가입
    public void userRegistration(UserRequestDto userRequestDto) {
        userRequestDto.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        userRequestDto.setAuthorities(Role.USER.getKey());

        userRepository.save(userRequestDto.toEntity());
    }

    public boolean duplicateCheck(String identifier) {
        if (userRepository.existsByIdentifier(identifier)) {
            throw new DuplicatedException("이미 등록된 아이디 입니다!");
        }

        return true;
    }

    // 유저 프로필 조회, 적립금 데이터만 반환시켜 줌
    public int getProfiles(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotExistUserException("존재하지 않는 유저입니다."));

        return user.getSavings();
    }

    // 유저 프로필 수정
    public UserResponseDto updateProfiles(UUID id, MeRequestDto meRequestDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotExistUserException("존재하지 않는 유저입니다."));

        User updatedUser = user.updateProfiles(meRequestDto);

        user = userRepository.save(updatedUser);

        return user.toResponseDto(user);
    }

    // 유저 탈퇴
    public void deleteProfiles(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotExistUserException("존재하지 않는 유저입니다."));

        User disabledUser = user.deleteProfiles();

        userRepository.save(disabledUser);
    }

    // 유저 비밀번호 변경
    public void updatePassword(UUID id, UpdatePasswordRequestDto passwordRequestDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotExistUserException("존재하지 않는 유저입니다."));
        String beforePassword = user.getPassword();

        if(!isPasswordEquals(beforePassword, passwordRequestDto.getOldPassword())) {
            throw new UpdatePasswordException("기존 비밀번호를 잘못 입력하였습니다.");
        }

        if (isPasswordEquals(beforePassword, passwordRequestDto.getNewPassword())) {
            throw new UpdatePasswordException("기존 비밀번호와 바꾸려는 새 비밀번호가 일치합니다.");
        }

        User updatedUser = user.updatePassword(passwordEncoder.encode(passwordRequestDto.getNewPassword()));

        userRepository.save(updatedUser);
    }

    private boolean isPasswordEquals(String password1, String password2) {
        return passwordEncoder.matches(password1, password2);
    }

}

package com.shoppingmall.service;

import com.shoppingmall.domain.NormalUser;
import com.shoppingmall.domain.SocialUser;
import com.shoppingmall.dto.*;
import com.shoppingmall.exception.DuplicatedException;
import com.shoppingmall.exception.UpdatePasswordException;
import com.shoppingmall.repository.NormalUserRepository;
import com.shoppingmall.repository.SocialUserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;


@AllArgsConstructor
@Slf4j
@Service
public class UserService {

    private NormalUserRepository normalUserRepository;
    private SocialUserRepository socialUserRepository;

    public String duplicateCheck(Map<String, Object> identifier) {
        Optional<NormalUser> normalUser = normalUserRepository.findByIdentifier((String) identifier.get("identifier"));

        String resultStr;

        if (normalUser.isPresent()) {
            throw new DuplicatedException("이미 등록된 아이디 입니다!");
        } else {
            resultStr = "SUCCESS";
        }

        return resultStr;
    }

    // 일반유저 회원가입
    public NormalUserResponseDto userRegistration(NormalUserRequestDto userRequestDto) throws Exception {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        userRequestDto.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        userRequestDto.setAuthorities("ROLE_USER");

        NormalUser normalUser = normalUserRepository.save(userRequestDto.toEntity());

        return NormalUserResponseDto.builder()
                .id(normalUser.getId())
                .identifier(normalUser.getIdentifier())
                .name(normalUser.getName())
                .email(normalUser.getEmail())
                .roadAddr(normalUser.getRoadAddr())
                .buildingName(normalUser.getBuildingName())
                .detailAddr(normalUser.getDetailAddr())
                .build();
    }

    // 일반유저 프로필 수정
    public NormalUserResponseDto updateProfiles(Long id, MeRequestDto meRequestDto) {

        NormalUser normalUser = normalUserRepository.findById(id).get();

        normalUser.setName(meRequestDto.getName());
        normalUser.setEmail(meRequestDto.getEmail());
        normalUser.setRoadAddr(meRequestDto.getRoadAddr());
        normalUser.setBuildingName(meRequestDto.getBuildingName());
        normalUser.setDetailAddr(meRequestDto.getDetailAddr());

        normalUser = normalUserRepository.save(normalUser);

        return NormalUserResponseDto.builder()
                .id(normalUser.getId())
                .identifier(normalUser.getIdentifier())
                .name(normalUser.getName())
                .email(normalUser.getEmail())
                .roadAddr(normalUser.getRoadAddr())
                .buildingName(normalUser.getBuildingName())
                .detailAddr(normalUser.getDetailAddr())
                .build();
    }

    // 일반유저 탈퇴
    public void deleteProfiles(Long id) {

        NormalUser normalUser = normalUserRepository.findById(id).get();

        normalUser.setDeleteYn('Y');

        normalUserRepository.save(normalUser);
    }

    // 일반유저 비밀번호 변경
    public void updatePassword(Long id, UpdatePasswordRequestDto passwordRequestDto) {

        NormalUser nomalUser = normalUserRepository.findById(id).get();

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (passwordEncoder.matches(passwordRequestDto.getNewPassword(), nomalUser.getPassword()))
            throw new UpdatePasswordException("기존 비밀번호와 바꾸려는 새 비밀번호가 일치합니다.");

        if (passwordEncoder.matches(passwordRequestDto.getOldPassword(), nomalUser.getPassword())) {
            nomalUser.setPassword(passwordEncoder.encode(passwordRequestDto.getNewPassword()));

            normalUserRepository.save(nomalUser);
        } else
            throw new UpdatePasswordException("기존 비밀번호를 잘못 입력하였습니다.");
    }

    // 소셜유저 프로필 수정
   public SocialUserResponseDto updateSocialProfiles(Long id, SocialUserRequestDto requestDto) {

        SocialUser socialUser = socialUserRepository.findById(id).get();

        socialUser.setRoadAddr(requestDto.getRoadAddr());
        socialUser.setBuildingName(requestDto.getBuildingName());
        socialUser.setDetailAddr(requestDto.getDetailAddr());
        socialUser.setUpdatedDate(LocalDateTime.now());

        socialUser = socialUserRepository.save(socialUser);

        return SocialUserResponseDto.builder()
               .id(socialUser.getId())
               .name(socialUser.getName())
               .email(socialUser.getEmail())
               .socialType(socialUser.getSocialType())
               .profileImage(socialUser.getProfileImage())
               .roadAddr(socialUser.getRoadAddr())
               .buildingName(socialUser.getBuildingName())
               .detailAddr(socialUser.getDetailAddr())
               .build();
    }
}

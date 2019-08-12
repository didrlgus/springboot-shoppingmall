package com.shoppingmall.service;

import com.shoppingmall.domain.NormalUser;
import com.shoppingmall.dto.MeRequestDto;
import com.shoppingmall.dto.NormalUserRequestDto;
import com.shoppingmall.dto.NormalUserResponseDto;
import com.shoppingmall.dto.UpdatePasswordRequestDto;
import com.shoppingmall.exception.DuplicatedException;
import com.shoppingmall.exception.NotExistUserException;
import com.shoppingmall.exception.UpdatePasswordException;
import com.shoppingmall.repository.NormalUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Service
public class NormalUserService {

    private NormalUserRepository normalUserRepository;

    // 일반유저 회원가입
    public void userRegistration(NormalUserRequestDto userRequestDto) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        userRequestDto.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        userRequestDto.setAuthorities("ROLE_USER");

        normalUserRepository.save(userRequestDto.toEntity());
    }

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

    // 유저 프로필 조회, 적립금 데이터만 반환시켜 줌
    public int getProfiles(Long id) {

        Optional<NormalUser> userOpt = normalUserRepository.findById(id);

        if (!userOpt.isPresent())
            throw new NotExistUserException("존재하지 않는 유저입니다.");

        return userOpt.get().getSavings();
    }

    // 일반유저 프로필 수정
    public NormalUserResponseDto updateProfiles(Long id, MeRequestDto meRequestDto) {

        Optional<NormalUser> userOpt = normalUserRepository.findById(id);

        if (!userOpt.isPresent())
            throw new NotExistUserException("존재하지 않는 유저입니다.");

        NormalUser normalUser = userOpt.get();

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
                .savings(normalUser.getSavings())
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
}

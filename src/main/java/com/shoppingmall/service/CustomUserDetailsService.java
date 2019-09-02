package com.shoppingmall.service;

import com.shoppingmall.domain.NormalUser;
import com.shoppingmall.domain.SecurityNormalUser;
import com.shoppingmall.dto.NormalUserResponseDto;
import com.shoppingmall.exception.DeleteUserException;
import com.shoppingmall.exception.NotExistUserException;
import com.shoppingmall.repository.NormalUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@AllArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private NormalUserRepository normalUserRepository;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        // 컨텍스트 홀더
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        Optional<NormalUser> normalUser = normalUserRepository.findByIdentifier(identifier);

        if (!normalUser.isPresent()) {
            throw new NotExistUserException("로그인에 실패하였습니다.");
        }

        NormalUserResponseDto normalUserResponseDto;

        if (normalUser.get().getDeleteYn().equals('Y')) {
            throw new DeleteUserException("이미 탈퇴된 유저입니다. 아이디를 다시 만들어주세요.");
        }

        normalUserResponseDto = normalUser.get().toResponseDto(normalUser.get());
        request.getSession().setAttribute("user", normalUserResponseDto);

        return Optional.of(normalUser)
                .map(SecurityNormalUser::new).get();
    }
}

package com.shoppingmall.service;

import com.shoppingmall.domain.SecurityNormalUser;
import com.shoppingmall.repository.NormalUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private NormalUserRepository normalUserRepository;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {

        return Optional.ofNullable(normalUserRepository.findByIdentifier(identifier))
                .filter(m -> m != null)
                .map(SecurityNormalUser::new).get();
    }
}

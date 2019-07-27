package com.shoppingmall.service;

import com.shoppingmall.domain.NormalUser;
import com.shoppingmall.repository.NormalUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class NormalUserService {

    private NormalUserRepository normalUserRepository;

    public void registration() {

        normalUserRepository.save(NormalUser.builder().identifier("didrlgus").name("양기현").build());
    }
}

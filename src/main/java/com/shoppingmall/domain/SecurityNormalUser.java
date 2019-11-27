package com.shoppingmall.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class SecurityNormalUser extends User {
    private static final String ROLE_PREFIX = "ROLE_";
    private static final long serialVersionUID = 1L;

    public SecurityNormalUser(NormalUser user) {
        super(user.getIdentifier(), user.getPassword(), makeGrantedAuthority(user.getAuthorities()));
    }

    private static List<GrantedAuthority> makeGrantedAuthority(String authorities){
        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new SimpleGrantedAuthority(authorities));

        return list;
    }
}
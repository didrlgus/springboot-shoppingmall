package com.shoppingmall.common;

import com.shoppingmall.domain.user.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SecurityUser extends org.springframework.security.core.userdetails.User {
    private static final String ROLE_PREFIX = "ROLE_";
    private static final long serialVersionUID = 1L;

    public SecurityUser(User user) {
        super(user.getIdentifier(), user.getPassword(), makeGrantedAuthority(user.getAuthorities()));
    }

    private static List<GrantedAuthority> makeGrantedAuthority(String authorities){
        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new SimpleGrantedAuthority(authorities));

        return list;
    }
}

package com.shoppingmall.repository;

import com.shoppingmall.domain.SocialUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialUserRepository extends JpaRepository<SocialUser, Long> {
    SocialUser findByEmail(String email);

    SocialUser findByPrincipal(String principal);
}

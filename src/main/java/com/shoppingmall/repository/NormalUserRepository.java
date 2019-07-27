package com.shoppingmall.repository;

import com.shoppingmall.domain.NormalUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NormalUserRepository extends JpaRepository<NormalUser, Long> {
    Optional<NormalUser> findByIdentifier(String identifier);
}
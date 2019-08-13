package com.shoppingmall.repository;

import com.shoppingmall.domain.Cart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Page<Cart> findAllByNormalUserIdAndUseYnOrderByCreatedDateDesc(Long userId, char useYn, Pageable pageable);

    List<Cart> findAllByNormalUserIdAndUseYnOrderByCreatedDateDesc(Long userId, char useYn);

    List<Cart> findAllByNormalUserIdAndProductId(Long userId, Long productId);

    List<Cart> findByCreatedDateBeforeAndUseYnEquals(LocalDateTime minusDays, char y);
}

package com.shoppingmall.domain.cart;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Page<Cart> findAllByUserIdAndUseYnOrderByCreatedDateDesc(UUID userId, char useYn, Pageable pageable);

    List<Cart> findAllByUserIdAndUseYnOrderByCreatedDateDesc(UUID userId, char useYn);

    List<Cart> findAllByUserIdAndProductId(UUID userId, Long productId);

}

package com.shoppingmall.domain.productOrder;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long> {
    Page<ProductOrder> findAllByUserIdOrderByCreatedDateDesc(UUID userId, Pageable pageable);
}

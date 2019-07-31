package com.shoppingmall.repository;

import com.shoppingmall.domain.ProductOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long> {
    Page<ProductOrder> findAllByNormalUserIdOrderByCreatedDateDesc(Long userId, Pageable pageable);
}

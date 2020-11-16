package com.shoppingmall.domain.productPurchaseCount;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductPurchaseCountRepository extends JpaRepository<ProductPurchaseCount, Long> {
}

package com.shoppingmall.domain.productPurchaseMergeCount;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductPurchaseMergeCountRepository extends JpaRepository<ProductPurchaseMergeCount, Long> {
}

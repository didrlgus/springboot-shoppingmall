package com.shoppingmall.domain.productPurchaseCountStandardTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductPurchaseCountStandardTimeRepository
        extends JpaRepository<ProductPurchaseCountStandardTime, Long>  {

    ProductPurchaseCountStandardTime findFirstByOrderByStandardTimeDesc();

}

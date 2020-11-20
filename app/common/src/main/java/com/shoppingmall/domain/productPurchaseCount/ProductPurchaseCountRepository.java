package com.shoppingmall.domain.productPurchaseCount;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Repository
public interface ProductPurchaseCountRepository extends JpaRepository<ProductPurchaseCount, Long> {
    ProductPurchaseCount findFirstByOrderByDateTimeAsc();

    @Transactional
    @Modifying
    @Query("delete from ProductPurchaseCount p where p.dateTime < :standardTime")
    void deleteAllByDateTimeBefore(@Param("standardTime") LocalDateTime standardTime);
}

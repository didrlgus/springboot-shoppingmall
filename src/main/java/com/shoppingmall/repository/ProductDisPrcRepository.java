package com.shoppingmall.repository;

import com.shoppingmall.domain.ProductDisPrc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ProductDisPrcRepository extends JpaRepository<ProductDisPrc, Long> {

    @Query("SELECT new Map(pdp.product AS product," +
            "MAX(pdp.disPrc) AS disPrc) " +
            "FROM ProductDisPrc pdp " +
            "WHERE NOW() BETWEEN pdp.startDt AND pdp.endDt ")
    Map<String, Object> getBestSaleProduct();

    @Query("SELECT new Map(pdp.id AS id, pdp.product AS product," +
            "MAX(pdp.disPrc) AS disPrc) " +
            "FROM ProductDisPrc pdp " +
            "WHERE NOW() BETWEEN pdp.startDt AND pdp.endDt " +
            "GROUP BY pdp.product")
    Page<Map<String, Object>> getSaleProductList(PageRequest pageable);

    List<ProductDisPrc> findByProductId(Long id);
}

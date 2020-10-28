package com.shoppingmall.domain.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findAll(Pageable pageable);

    Page<Product> findBySmallCatCd(String catCd, Pageable pageable);

    Page<Product> findAllByLargeCatCd(String largeCatCd, Pageable pageable);

    // N+1 문제 발생
    // 1. fetch join
    // 2. @EntityGraph로 해결 가능, @EntityGraph(attributePaths = {"productDisPrcList"})
    // 3. BatchSize로 해결 가능 (batch size만큼의 product를 미리 가져옴)
    // 1,2번은 paging 처리에서 문제 발생, 그래서 3번 선택
    @Query("select p from Product p")
    List<Product> findBestTop10Products(Pageable pageable);

    @Query("select p from Product p")
    List<Product> findNewTop8Products(Pageable pageable);

    Page<Product> findByLargeCatCdAndSmallCatCdOrderByCreatedDateDesc(String firstCatCd, String secondCatCd, Pageable pageable);

}

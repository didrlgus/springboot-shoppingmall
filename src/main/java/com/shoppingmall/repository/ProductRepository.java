package com.shoppingmall.repository;

import com.shoppingmall.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {

    Page<Product> findAll(Pageable pageable);

    Page<Product> findBySmallCatCd(String catCd, Pageable pageable);

    Page<Product> findAllByLargeCatCd(String largeCatCd, Pageable pageable);

    List<Product> findTop10ByOrderByPurchaseCountDesc();

    List<Product> findTop8ByOrderByCreatedDateDesc();

    Page<Product> findByLargeCatCdAndSmallCatCdOrderByCreatedDateDesc(String firstCatCd, String secondCatCd, Pageable pageable);
}

package com.shoppingmall.repository;

import com.shoppingmall.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAll(Pageable pageable);

    Page<Product> findBySmallCatCd(String catCd, Pageable pageable);

    Page<Product> findAllByLargeCatCd(String largeCatCd, Pageable pageable);
}

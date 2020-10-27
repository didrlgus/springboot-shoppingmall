package com.shoppingmall.domain.product;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p from Product p")
    List<Product> findBestTop10Products(Pageable pageable);

    @Query("select p from Product p")
    List<Product> findNewTop8Products(Pageable pageable);

}

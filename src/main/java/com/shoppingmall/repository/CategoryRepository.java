package com.shoppingmall.repository;

import com.shoppingmall.domain.ProductCat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CategoryRepository extends JpaRepository<ProductCat, Long> {
}

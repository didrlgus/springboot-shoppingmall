package com.shoppingmall.repository;

import com.shoppingmall.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductRepositoryCustom {

    List<Product> getRelatedProductList(Long id, String smallCatCd);

    Page<Product> findSaleProductList(PageRequest pageable);

    // List<Product> findSaleProducts(Pageable pageable);
}

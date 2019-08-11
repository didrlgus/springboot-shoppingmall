package com.shoppingmall.repository;

import com.shoppingmall.domain.Product;

import java.util.List;

public interface ProductRepositoryCustom {

    List<Product> getRelatedProductList(Long id, String smallCatCd);
}

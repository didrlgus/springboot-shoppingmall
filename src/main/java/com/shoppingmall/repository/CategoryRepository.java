package com.shoppingmall.repository;

import com.shoppingmall.domain.ProductCat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CategoryRepository extends JpaRepository<ProductCat, Long> {
    List<ProductCat> findAllByCatLvOrderByCatCdDesc(int i);

    List<ProductCat> findAllByUpprCatCdOrderByCatCdDesc(String upprCatCd);

    ProductCat findByCatCd(String upprCatCd);

    List<ProductCat> findAllByUseYn(char y);

    List<ProductCat> findAllByUpprCatCd(String firstCatCd);
}

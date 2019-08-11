package com.shoppingmall.repository;


import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shoppingmall.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.shoppingmall.domain.QProduct.product;
import static com.shoppingmall.domain.QProductDisPrc.productDisPrc;

@RequiredArgsConstructor
@Repository
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    // 관련 상품 인기 순, 최신 순으로 4개 뽑아내기 (자기 자신은 제외해야 함)
    @Override
    public List<Product> getRelatedProductList(Long id, String smallCatCd) {

        return queryFactory.selectFrom(product)
                .leftJoin(product.productDisPrcList, productDisPrc)
                .where(product.id.ne(id), product.smallCatCd.eq(smallCatCd))
                .orderBy(product.purchaseCount.desc(), product.createdDate.desc())
                .distinct()
                .limit(4)
                .fetchResults().getResults();
    }
}

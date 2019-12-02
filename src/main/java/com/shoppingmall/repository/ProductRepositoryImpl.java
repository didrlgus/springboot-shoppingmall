package com.shoppingmall.repository;


import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shoppingmall.domain.Product;
import com.shoppingmall.domain.QProduct;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.shoppingmall.domain.QProduct.product;
import static com.shoppingmall.domain.QProductDisPrc.productDisPrc;

@Repository
public class ProductRepositoryImpl extends QuerydslRepositorySupport implements ProductRepositoryCustom {

    public ProductRepositoryImpl() {
        super(Product.class);
    }

    @Autowired
    private JPAQueryFactory queryFactory;

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

    /*@Override
    public List<Product> findSaleProducts(Pageable pageable) {

        return queryFactory.selectFrom(product)
                .leftJoin(product.productDisPrcList, product).fetchJoin()
                .orderBy(product.updatedDate.desc())
                .distinct().fetchResults().getResults();
    }*/

    @Override
    public Page<Product> findSaleProductList(PageRequest pageable) {
        JPQLQuery<Product> query
                = from(product).innerJoin(product.productDisPrcList, productDisPrc).fetchJoin()
                  .where().orderBy(product.updatedDate.desc()).distinct();

        /*List<Product> query = queryFactory
                .selectFrom(product)
                .innerJoin(product.productDisPrcList, productDisPrc).fetchJoin()
                .orderBy(product.updatedDate.desc())
                .distinct()
                .limit(10)
                .offset(10)
                .fetchResults().getResults();*/

        List<Product> productList = getQuerydsl().applyPagination(pageable, query).fetch();

        return new PageImpl<>(productList, pageable, query.fetchCount());
    }

    /*@Override
    public List<Product> findSaleProductList(Pageable pageable) {

        return queryFactory
                .selectFrom(product)
                .innerJoin(product.productDisPrcList, productDisPrc).fetchJoin()
                .orderBy(product.updatedDate.desc())
                .distinct()
                .fetchResults().getResults();

        *//*return products.stream()
                .map(p -> new Family(p.getName(), p.getChildren()))
                .collect(Collectors.toList());*//*
    }*/
}

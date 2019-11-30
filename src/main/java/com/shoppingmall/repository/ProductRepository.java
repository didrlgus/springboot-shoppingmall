package com.shoppingmall.repository;

import com.shoppingmall.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {

    Page<Product> findAll(Pageable pageable);

    Page<Product> findBySmallCatCd(String catCd, Pageable pageable);

    Page<Product> findAllByLargeCatCd(String largeCatCd, Pageable pageable);

    // N+1 문제 발생
    // @EntityGraph로 해결 -> left outer join 이 가능
    // fetch join은 inner join 이므로 여기서는 사용 x
    @EntityGraph(attributePaths = {"productDisPrcList"})
    @Query("select p from Product p order by p.purchaseCount desc")
    List<Product> findTop10ByOrderByPurchaseCountDesc();

    List<Product> findTop8ByOrderByCreatedDateDesc();

    Page<Product> findByLargeCatCdAndSmallCatCdOrderByCreatedDateDesc(String firstCatCd, String secondCatCd, Pageable pageable);
}

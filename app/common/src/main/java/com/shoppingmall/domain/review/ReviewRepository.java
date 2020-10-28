package com.shoppingmall.domain.review;

import com.shoppingmall.domain.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findAllByProductIdOrderByCreatedDateDesc(Long productId, Pageable pageable);

    @Query("SELECT AVG(r.rate) AS rateAvg " +
            "FROM Review r " +
            "WHERE r.product = ?1")
    double getReviewRateAvg(Product product);
}

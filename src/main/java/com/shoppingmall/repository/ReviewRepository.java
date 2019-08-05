package com.shoppingmall.repository;

import com.shoppingmall.domain.Product;
import com.shoppingmall.domain.Review;
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

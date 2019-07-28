package com.shoppingmall.repository;

import com.shoppingmall.domain.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    Optional<Question> findByProductId(Long id);

    Page<Question> findAllByProductIdOrderByCreatedDateDesc(Long id, Pageable pageable);
}

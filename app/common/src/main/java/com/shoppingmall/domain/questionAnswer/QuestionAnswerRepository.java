package com.shoppingmall.domain.questionAnswer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionAnswerRepository extends JpaRepository<QuestionAnswer, Long> {
    Page<QuestionAnswer> findAllByQuestionIdOrderByCreatedDateDesc(Long questionId, Pageable pageable);
}

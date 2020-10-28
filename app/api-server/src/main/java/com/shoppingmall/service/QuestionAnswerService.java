package com.shoppingmall.service;

import com.shoppingmall.domain.question.Question;
import com.shoppingmall.domain.question.QuestionRepository;
import com.shoppingmall.domain.questionAnswer.QuestionAnswer;
import com.shoppingmall.domain.questionAnswer.QuestionAnswerRepository;
import com.shoppingmall.domain.user.User;
import com.shoppingmall.domain.user.UserRepository;
import com.shoppingmall.dto.PagingDto;
import com.shoppingmall.dto.QuestionAnswerRequestDto;
import com.shoppingmall.dto.QuestionAnswerResponseDto;
import com.shoppingmall.exception.NotExistQuestionException;
import com.shoppingmall.exception.NotExistUserException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class QuestionAnswerService {

    private UserRepository userRepository;
    private QuestionRepository questionRepository;
    private QuestionAnswerRepository questionAnswerRepository;

    public HashMap<String, Object> getAnswer(Long questionId, int page, Pageable pageable) {
        int realPage = (page == 0) ? 0 : (page - 1);
        pageable = PageRequest.of(realPage, 3);

        Page<QuestionAnswer> answerList
                = questionAnswerRepository.findAllByQuestionIdOrderByCreatedDateDesc(questionId, pageable);

        List<QuestionAnswerResponseDto> answerDtoList = new ArrayList<>();

        for (QuestionAnswer answer : answerList) {
            answerDtoList.add(answer.toDto());
        }

        PageImpl<QuestionAnswerResponseDto> answers = new PageImpl<>(answerDtoList, pageable, answerList.getTotalElements());

        HashMap<String, Object> resultMap = new HashMap<>();

        PagingDto answerPagingDto = new PagingDto();
        answerPagingDto.setPagingInfo(answers);

        resultMap.put("answerList", answers);
        resultMap.put("answerPagingDto", answerPagingDto);

        return resultMap;
    }

    @Transactional
    public void makeAnswer(Long questionId, QuestionAnswerRequestDto questionAnswerRequestDto) {

        User normalUser = userRepository.findById(questionAnswerRequestDto.getUserId())
                .orElseThrow(() -> new NotExistUserException("존재하지 않는 유저입니다."));

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new NotExistQuestionException("존재하지 않는 문의입니다."));

        // 댓글 상태, 개수 추가
        if (!question.isAnswerState()) {
            question.setAnswerState(true);
        }

        question.setAnswerCount(question.getAnswerCount() + 1);
        questionRepository.save(question);

        questionAnswerRepository.save(QuestionAnswer.builder()
                .user(normalUser)
                .question(question)
                .message(questionAnswerRequestDto.getMessage())
                .build());
    }
}

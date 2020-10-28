package com.shoppingmall.service;

import com.shoppingmall.domain.product.Product;
import com.shoppingmall.domain.product.ProductRepository;
import com.shoppingmall.domain.question.Question;
import com.shoppingmall.domain.question.QuestionRepository;
import com.shoppingmall.domain.user.User;
import com.shoppingmall.domain.user.UserRepository;
import com.shoppingmall.exception.NotExistQuestionException;
import com.shoppingmall.dto.PagingDto;
import com.shoppingmall.dto.QuestionRequestDto;
import com.shoppingmall.dto.QuestionResponseDto;
import com.shoppingmall.exception.NotExistProductException;
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
public class QuestionService {

    private UserRepository userRepository;
    private ProductRepository productRepository;
    private QuestionRepository questionRepository;

    @Transactional
    public void makeQuestion(QuestionRequestDto questionRequestDto) {
        User user = userRepository.findById(questionRequestDto.getUserId()).orElseThrow(()
                -> new NotExistUserException("존재하지 않는 유저입니다."));

        Product product = productRepository.findById(questionRequestDto.getProductId()).orElseThrow(()
                -> new NotExistProductException("존재하지 않는 상품입니다."));

        Question question = Question.builder()
                .user(user)
                .product(product)
                .message(questionRequestDto.getMessage())
                .answerCount(0)
                .answerState(false)
                .build();

        questionRepository.save(question);
    }

    public HashMap<String, Object> getQuestionList(Long productId, int page) {
        // 페이지가 0부터 시작하므로 1을 빼주어야 함
        int realPage = (page == 0) ? 0 : (page - 1);
        Pageable pageable = PageRequest.of(realPage, 3);

        Page<Question> questionList = questionRepository.findAllByProductIdOrderByCreatedDateDesc(productId, pageable);

        List<QuestionResponseDto> questionResponseDtoList = new ArrayList<>();
        HashMap<String, Object> resultMap = new HashMap<>();

        if (questionList.getTotalElements() > 0) {
            for (Question question : questionList) {
                questionResponseDtoList.add(question.toResponseDto());
            }
        }

        PageImpl<QuestionResponseDto> questions = new PageImpl<>(questionResponseDtoList, pageable, questionList.getTotalElements());

        PagingDto questionPagingDto = new PagingDto();
        questionPagingDto.setPagingInfo(questions);

        resultMap.put("questionList", questions);
        resultMap.put("questionPagingDto", questionPagingDto);

        return resultMap;
    }

    public void updateQuestion(Long id, QuestionRequestDto.Update questionRequestDto) {

        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new NotExistQuestionException("존재하지 않는 문의 입니다."));

        question.setMessage(questionRequestDto.getMessage());

        questionRepository.save(question);
    }

    public void deleteQuestion(Long id) {

        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new NotExistQuestionException("존재하지 않는 문의 입니다."));

        questionRepository.delete(question);
    }
}

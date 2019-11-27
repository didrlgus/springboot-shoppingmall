package com.shoppingmall.service;

import com.shoppingmall.domain.NormalUser;
import com.shoppingmall.domain.Product;
import com.shoppingmall.domain.Question;
import com.shoppingmall.exception.NotExistQuestionException;
import com.shoppingmall.dto.PagingDto;
import com.shoppingmall.dto.QuestionRequestDto;
import com.shoppingmall.dto.QuestionResponseDto;
import com.shoppingmall.exception.NotExistProductException;
import com.shoppingmall.exception.NotExistUserException;
import com.shoppingmall.repository.NormalUserRepository;
import com.shoppingmall.repository.ProductRepository;
import com.shoppingmall.repository.QuestionRepository;
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
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class QuestionService {

    private NormalUserRepository normalUserRepository;
    private ProductRepository productRepository;
    private QuestionRepository questionRepository;

    @Transactional
    public void makeQuestion(QuestionRequestDto questionRequestDto, Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, 3);

        Optional<NormalUser> normalUser = normalUserRepository.findById(questionRequestDto.getNormalUserId());

        if (!normalUser.isPresent())
            throw new NotExistUserException("존재하지 않는 유저입니다.");

        Optional<Product> product = productRepository.findById(questionRequestDto.getProductId());

        if (!product.isPresent())
            throw new NotExistProductException("존재하지 않는 상품입니다.");

        Question question = Question.builder()
                .normalUser(normalUser.get())
                .product(product.get())
                .message(questionRequestDto.getMessage())
                .answerCount(0)
                .answerState(false)
                .build();

        questionRepository.save(question);

        Page<Question> questionList = questionRepository.findAllByProductIdOrderByCreatedDateDesc(product.get().getId(), pageable);
    }

    public HashMap<String, Object> getQuestionList(Long productId, int page, Pageable pageable) {
        // 페이지가 0부터 시작하므로 1을 빼주어야 함
        int realPage = (page == 0) ? 0 : (page - 1);
        pageable = PageRequest.of(realPage, 3);

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

        Optional<Question> question = questionRepository.findById(id);

        if (!question.isPresent()) {
            throw new NotExistQuestionException("존재하지 않는 문의 입니다.");
        }

        question.get().setMessage(questionRequestDto.getMessage());

        questionRepository.save(question.get());
    }

    public void deleteQuestion(Long id) {

        Optional<Question> question = questionRepository.findById(id);

        if (!question.isPresent()) {
            throw new NotExistQuestionException("존재하지 않는 문의 입니다.");
        }

        questionRepository.delete(question.get());
    }
}

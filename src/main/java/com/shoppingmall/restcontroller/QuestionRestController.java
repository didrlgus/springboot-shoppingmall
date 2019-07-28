package com.shoppingmall.restcontroller;


import com.shoppingmall.dto.QuestionRequestDto;
import com.shoppingmall.service.QuestionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@AllArgsConstructor
@RestController
public class QuestionRestController {

    private QuestionService questionService;

    @PostMapping("/question")
    public ResponseEntity<?> makeQuestion(@RequestBody @Valid QuestionRequestDto questionRequestDto,
                                          BindingResult bindingResult,
                                          @PageableDefault(size = 3, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {

        if (bindingResult.hasErrors()){
            String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }

        questionService.makeQuestion(questionRequestDto, pageable);

        log.info("#### question {}", questionRequestDto);

        return ResponseEntity.ok("문의가 등록되었습니다.");
    }

    @GetMapping("/product/{id}/question/{page}")
    public ResponseEntity<?> getQuestionList(@PathVariable("id") Long productId, @PathVariable("page") int page,
                                             @PageableDefault(size = 3, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {

        return ResponseEntity.ok().body(questionService.getQuestionList(productId, page, pageable));
    }

    @PutMapping("/question/{id}")
    public ResponseEntity<?> updateQuestion(@RequestBody @Valid QuestionRequestDto.Update questionRequestDto,
                                            @PathVariable Long id,
                                            BindingResult bindingResult) {

        if (bindingResult.hasErrors()){
            String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }

        questionService.updateQuestion(id, questionRequestDto);

        return ResponseEntity.ok().body("문의가 수정되었습니다.");
    }

    @DeleteMapping("/question/{id}")
    public ResponseEntity<?> deleteQuestion(@PathVariable Long id) {

        questionService.deleteQuestion(id);

        return ResponseEntity.ok().body("삭제되었습니다.");
    }

}

package com.shoppingmall.restcontroller;

import com.shoppingmall.dto.QuestionAnswerRequestDto;
import com.shoppingmall.service.QuestionAnswerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@AllArgsConstructor
@Api(tags = "questionAnswer", description = "댓글")
@RestController
public class QuestionAnswerRestController {

    private QuestionAnswerService questionAnswerService;

    // 댓글 조회
    @ApiOperation(value = "댓글 조회")
    @GetMapping("/questions/{questionId}/answers/{page}")
    public ResponseEntity<?> getAnswer(@PathVariable("questionId") Long questionId, @PathVariable("page") int page,
                                       @PageableDefault(size = 3, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {


        return ResponseEntity.ok().body(questionAnswerService.getAnswer(questionId, page, pageable));
    }

    // 댓글 추가
    @ApiOperation(value = "댓글 생성")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("/questions/{questionId}/answers")
    public ResponseEntity<?> makeAnswer(@RequestBody @Valid QuestionAnswerRequestDto questionAnswerRequestDto,
                                          @PathVariable Long questionId,
                                          BindingResult bindingResult) {

        if (bindingResult.hasErrors()){
            String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }

        // 댓글 추가
        questionAnswerService.makeAnswer(questionId, questionAnswerRequestDto);

        return ResponseEntity.ok("댓글이 추가되었습니다!");
    }
}

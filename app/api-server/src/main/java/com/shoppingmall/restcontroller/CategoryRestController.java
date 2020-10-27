package com.shoppingmall.restcontroller;

import com.shoppingmall.dto.CategoryRequestDto;
import com.shoppingmall.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@AllArgsConstructor
@Api(tags = "category", description = "카테고리")
@RestController
public class CategoryRestController {

    private CategoryService categoryService;

    // 전체 카테고리 조회
    @ApiOperation(value = "전체 카테고리 조회")
    @GetMapping("/categories")
    public ResponseEntity<?> getCategory() {

        return ResponseEntity.ok().body(categoryService.getCategoryList());
    }

    // 1차 카테고리 추가 (관리자 권한)
    @ApiOperation(value = "1차 카테고리 추가 (관리자 권한)")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/categories/first")
    public ResponseEntity<?> addFirstCategory(@RequestBody @Valid CategoryRequestDto.firstCategory firstCategory,
                                              BindingResult bindingResult) {

        if (bindingResult.hasErrors()){
            String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok().body(categoryService.addFirstCategory(firstCategory));
    }

    // 2차 카테고리 추가 (관리자 권한)
    @ApiOperation(value = "2차 카테고리 추가 (관리자 권한)")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/categories/second")
    public ResponseEntity<?> addSecondCategory(@RequestBody @Valid CategoryRequestDto.secondCategory secondCategory,
                                               BindingResult bindingResult) {

        if (bindingResult.hasErrors()){
            String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok().body(categoryService.addSecondCategory(secondCategory));
    }

    // 1차 or 2차 카테고리 상세 (관리자 권한)
    @ApiOperation(value = "1, 2차 카테고리 상세 (관리자 권한)")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/categories/{id}")
    public ResponseEntity<?> getDetailOfCategory(@PathVariable Long id) {

        return categoryService.getDetailOfCategory(id);
    }

    // 1차 or 2차 카테고리 수정 (관리자 권한)
    @ApiOperation(value = "1, 2차 카테고리 수정 (관리자 권한)")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/categories/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody @Valid CategoryRequestDto categoryDto,
                                            BindingResult bindingResult) {

        if (bindingResult.hasErrors()){
            String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok().body(categoryService.updateCategory(id, categoryDto));
    }

    // 해당 1차 카테고리에 속하는 2차 카테고리 조회하기
    @ApiOperation(value = "1차 카테고리에 속한 2차 카테고리 조회 (관리자 권한)")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/categories/first/{firstCatCd}/second")
    public ResponseEntity<?> getSecondCategory(@PathVariable String firstCatCd) {

        return ResponseEntity.ok().body(categoryService.getSecondCategoryList(firstCatCd));
    }
}

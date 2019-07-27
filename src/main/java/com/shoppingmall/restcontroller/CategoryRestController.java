package com.shoppingmall.restcontroller;

import com.shoppingmall.service.CategoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
public class CategoryRestController {

    private CategoryService categoryService;

    @GetMapping("/category")
    public ResponseEntity<?> getCategory() {

        return ResponseEntity.ok().body(categoryService.getCategoryList());
    }
}

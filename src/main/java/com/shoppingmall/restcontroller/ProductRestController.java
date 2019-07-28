package com.shoppingmall.restcontroller;

import com.shoppingmall.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
public class ProductRestController {

    private ProductService productService;

    @GetMapping("/productList/{page}/catCd/{catCd}")
    public ResponseEntity<?> getProductList(@PathVariable("page") int page, @PathVariable("catCd") String catCd,
                                            @PageableDefault(size = 9, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {

        return ResponseEntity.ok().body(productService.getProductListByCategory(catCd, pageable, page));
    }
}

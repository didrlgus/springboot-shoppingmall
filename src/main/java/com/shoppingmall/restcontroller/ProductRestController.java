package com.shoppingmall.restcontroller;

import com.shoppingmall.dto.ProductResponseDto;
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

import java.util.List;

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

    @GetMapping("/product/{id}")
    public ResponseEntity<?> getProductDetails(@PathVariable Long id) {

        return ResponseEntity.ok().body(productService.getProductDetails(id));
    }

    @GetMapping("/productList/{page}/catCd/{catCd}/sortCd/{sortCd}")
    public ResponseEntity<?> getProductListByKeyword(@PathVariable("page") int page, @PathVariable("catCd") String catCd,
                                                     @PathVariable("sortCd") String sortCd) {

        return ResponseEntity.ok().body(productService.getProductListByKeyword(page, catCd, sortCd));
    }

    @GetMapping("/productList/best")
    public ResponseEntity<?> getBestProductList() {

        return ResponseEntity.ok().body(productService.getBestProductList());
    }
}

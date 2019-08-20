package com.shoppingmall.restcontroller;

import com.shoppingmall.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/productList/sale/{page}")
    public ResponseEntity<?> getSaleProductList(@PathVariable int page) {

        return ResponseEntity.ok().body(productService.getSaleProductList(page));
    }

    @GetMapping("/product/{id}/relation/{smallCatCd}")
    public ResponseEntity<?> getRelatedProductList(@PathVariable("id") Long id, @PathVariable("smallCatCd") String smallCatCd) {

        return ResponseEntity.ok().body(productService.getRelatedProductList(id, smallCatCd));
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/productList/{page}")
    public ResponseEntity<?> getAdminProductList(@PathVariable int page) {

        return ResponseEntity.ok().body(productService.getAdminProductList(page));
    }

    // 1차 카테고리 코드와 2차 카테고리 코드로 상품 리스트 조회하기
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/productList/{page}/firstCategory/{firstCatCd}/secondCategory/{secondCatCd}")
    public ResponseEntity<?> getProductListByCatCd(@PathVariable("page") int page, @PathVariable("firstCatCd") String firstCatCd,
                                                   @PathVariable("secondCatCd") String secondCatCd) {

        return ResponseEntity.ok().body(productService.getProductListByCatCd(page, firstCatCd, secondCatCd));
    }

}

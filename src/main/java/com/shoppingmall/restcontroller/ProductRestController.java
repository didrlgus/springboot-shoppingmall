package com.shoppingmall.restcontroller;

import com.shoppingmall.domain.UploadFile;
import com.shoppingmall.dto.ProductRequestDto;
import com.shoppingmall.service.ProductService;
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
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

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

    // 상품 리스트 조회 (관리자 권한)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/productList/{page}")
    public ResponseEntity<?> getAdminProductList(@PathVariable int page) {

        return ResponseEntity.ok().body(productService.getAdminProductList(page));
    }

    // 1차 카테고리 코드와 2차 카테고리 코드로 상품 리스트 조회하기 (관리자 권한)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/productList/{page}/firstCategory/{firstCatCd}/secondCategory/{secondCatCd}")
    public ResponseEntity<?> getProductListByCatCd(@PathVariable("page") int page, @PathVariable("firstCatCd") String firstCatCd,
                                                   @PathVariable("secondCatCd") String secondCatCd) {

        return ResponseEntity.ok().body(productService.getProductListByCatCd(page, firstCatCd, secondCatCd));
    }

    // 상품 타이틀 이미지 업로드 (관리자 권한)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/product/titleImage")
    public ResponseEntity<?> uploadReviewImage(@RequestParam("file") MultipartFile file) {

        try {
            UploadFile uploadedFile = productService.uploadProductImage(file);
            return ResponseEntity.ok().body("product-upload-image/" + uploadedFile.getSaveFileName());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 상품 추가 (관리자 권한)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestBody @Valid ProductRequestDto productRequestDto,
                                        BindingResult bindingResult) {

        if (bindingResult.hasErrors()){
            String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok().body(productService.addProduct(productRequestDto));
    }

    // 상품 상세 (관리자 권한)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/product/{id}")
    public ResponseEntity<?> getAdminProductDetails(@PathVariable Long id) {

        return ResponseEntity.ok().body(productService.getAdminProductDetails(id));
    }

    // 상품 수정 (관리자 권한)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/product/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody @Valid ProductRequestDto.UpdateRequestDto updateRequestDto,
                                           BindingResult bindingResult) {

        if (bindingResult.hasErrors()){
            String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok().body(productService.updateProduct(id, updateRequestDto));
    }

    // 상품 삭제 (관리자 권한)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/product/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {

        return ResponseEntity.ok().body(productService.deleteProduct(id));
    }
}

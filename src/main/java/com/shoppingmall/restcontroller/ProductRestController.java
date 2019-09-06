package com.shoppingmall.restcontroller;

import com.shoppingmall.domain.UploadFile;
import com.shoppingmall.dto.ProductRequestDto;
import com.shoppingmall.service.ProductService;
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
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Slf4j
@AllArgsConstructor
@Api(tags = "product", description = "상품")
@RestController
public class ProductRestController {

    private ProductService productService;

    @ApiOperation(value = "상품 전체 조회")
    @GetMapping("/productList/{page}/catCd/{catCd}")
    public ResponseEntity<?> getProductList(@PathVariable("page") int page, @PathVariable("catCd") String catCd,
                                            @PageableDefault(size = 9, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {

        return ResponseEntity.ok().body(productService.getProductListByCategory(catCd, pageable, page));
    }

    @ApiOperation(value = "상품 상세")
    @GetMapping("/products/{id}")
    public ResponseEntity<?> getProductDetails(@PathVariable Long id) {

        return ResponseEntity.ok().body(productService.getProductDetails(id));
    }

    @ApiOperation(value = "키워드로 상품 조회")
    @GetMapping("/productList/{page}/catCd/{catCd}/sortCd/{sortCd}")
    public ResponseEntity<?> getProductListByKeyword(@PathVariable("page") int page, @PathVariable("catCd") String catCd,
                                                     @PathVariable("sortCd") String sortCd) {

        return ResponseEntity.ok().body(productService.getProductListByKeyword(page, catCd, sortCd));
    }

    @ApiOperation(value = "인기 상품 조회")
    @GetMapping("/productList/best")
    public ResponseEntity<?> getBestProductList() {

        return ResponseEntity.ok().body(productService.getBestProductList());
    }

    @ApiOperation(value = "할인 상품 조회")
    @GetMapping("/productList/sale/{page}")
    public ResponseEntity<?> getSaleProductList(@PathVariable int page) {

        return ResponseEntity.ok().body(productService.getSaleProductList(page));
    }

    @ApiOperation(value = "관련 상품 조회")
    @GetMapping("/products/{id}/relation/{smallCatCd}")
    public ResponseEntity<?> getRelatedProductList(@PathVariable("id") Long id, @PathVariable("smallCatCd") String smallCatCd) {

        return ResponseEntity.ok().body(productService.getRelatedProductList(id, smallCatCd));
    }

    // 상품 리스트 조회 (관리자 권한)
    @ApiOperation(value = "상품 전체 조회 (관리자 권한)")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/productList/{page}")
    public ResponseEntity<?> getAdminProductList(@PathVariable int page) {

        return ResponseEntity.ok().body(productService.getAdminProductList(page));
    }

    // 1차 카테고리 코드와 2차 카테고리 코드로 상품 리스트 조회하기 (관리자 권한)
    @ApiOperation(value = "카테고리를 통한 상품 조회 (관리자 권한)")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/productList/{page}/firstCategory/{firstCatCd}/secondCategory/{secondCatCd}")
    public ResponseEntity<?> getProductListByCatCd(@PathVariable("page") int page, @PathVariable("firstCatCd") String firstCatCd,
                                                   @PathVariable("secondCatCd") String secondCatCd) {

        return ResponseEntity.ok().body(productService.getProductListByCatCd(page, firstCatCd, secondCatCd));
    }

    // 상품 타이틀 이미지 업로드 (관리자 권한)
    @ApiOperation(value = "상품 타이틀 이미지 업로드 (관리자 권한)")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/products/titleImage")
    public ResponseEntity<?> uploadReviewImage(@RequestParam("file") MultipartFile file) {

        try {
            UploadFile uploadedFile = productService.uploadProductImage(file);
            return ResponseEntity.ok().body("product-upload-image/" + uploadedFile.getSaveFileName());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 상품 추가 (관리자 권한)
    @ApiOperation(value = "상품 추가 (관리자 권한)")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/products")
    public ResponseEntity<?> addProduct(@RequestBody @Valid ProductRequestDto productRequestDto,
                                        BindingResult bindingResult) {

        if (bindingResult.hasErrors()){
            String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok().body(productService.addProduct(productRequestDto));
    }

    // 상품 상세 (관리자 권한)
    @ApiOperation(value = "상품 상세 (관리자 권한)")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/products/{id}")
    public ResponseEntity<?> getAdminProductDetails(@PathVariable Long id) {

        return ResponseEntity.ok().body(productService.getAdminProductDetails(id));
    }

    // 상품 수정 (관리자 권한)
    @ApiOperation(value = "상품 수정 (관리자 권한)")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/products/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody @Valid ProductRequestDto.UpdateRequestDto updateRequestDto,
                                           BindingResult bindingResult) {

        if (bindingResult.hasErrors()){
            String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok().body(productService.updateProduct(id, updateRequestDto));
    }

    // 상품 삭제 (관리자 권한)
    /*@ApiOperation(value = "상품 삭제 (관리자 권한)")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {

        return ResponseEntity.ok().body(productService.deleteProduct(id));
    }*/
}

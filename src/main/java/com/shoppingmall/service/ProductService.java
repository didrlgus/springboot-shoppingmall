package com.shoppingmall.service;

import com.shoppingmall.domain.Product;
import com.shoppingmall.domain.ProductCat;
import com.shoppingmall.dto.PagingDto;
import com.shoppingmall.dto.ProductResponseDto;
import com.shoppingmall.exception.NoValidProductSortException;
import com.shoppingmall.exception.NotExistProductException;
import com.shoppingmall.repository.CategoryRepository;
import com.shoppingmall.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class ProductService {

    private ProductRepository productRepository;

    // 전체 상품 혹은 카테고리로 상품 조회
    public HashMap<String, Object> getProductListByCategory(String catCd, Pageable pageable, int page) {
        int realPage = page - 1;

        pageable = PageRequest.of(realPage, 9, new Sort(Sort.Direction.DESC, "createdDate"));

        Page<Product> productList;

        if (catCd.equals("ALL")) {
            productList = productRepository.findAll(pageable);
        } else {
            productList = productRepository.findBySmallCatCd(catCd, pageable);
        }

        List<ProductResponseDto> productResponseDtoList = new ArrayList<>();

        for (Product product : productList) {
            productResponseDtoList.add(product.toResponseDto());
        }

        PageImpl<ProductResponseDto> products = new PageImpl<>(productResponseDtoList, pageable, productList.getTotalElements());
        
        return getResultMap(products);
    }

    // 상품 상세
    public ProductResponseDto getProductDetails(Long id) {
        Optional<Product> productDetails = productRepository.findById(id);

        if (productDetails.isPresent())
            return productDetails.get().toResponseDto();
        else
            throw new NotExistProductException("존재하지 않는 상품입니다.");
    }

    public HashMap<String, Object> getProductListByKeyword(int page, String largeCatCd, String sortCd) {
        int realPage = page - 1;

        PageImpl<ProductResponseDto> productResponseDtoPage = checkSort(realPage, largeCatCd, sortCd);

        return getResultMap(productResponseDtoPage);
    }

    private PageImpl<ProductResponseDto> checkSort(int realPage, String largeCatCd, String sortCd) {
        Pageable pageable;
        Page<Product> productList;

        if (largeCatCd.equals("ALL")) {
            pageable = getPageable(realPage, sortCd);

            productList = productRepository.findAll(pageable);
        } else {
            pageable = getPageable(realPage, sortCd);

            productList = productRepository.findAllByLargeCatCd(largeCatCd, pageable);
        }

        List<ProductResponseDto> productResponseDtoList = new ArrayList<>();

        for (Product product : productList) {
            productResponseDtoList.add(product.toResponseDto());
        }

        return new PageImpl<>(productResponseDtoList, pageable, productList.getTotalElements());
    }

    private Pageable getPageable(int realPage, String sortCd) {
        Pageable pageable;

        switch (sortCd) {
            case "new":
                pageable = PageRequest.of(realPage, 9, new Sort(Sort.Direction.DESC, "createdDate"));
                break;
            case "past":
                pageable = PageRequest.of(realPage, 9, new Sort(Sort.Direction.ASC, "createdDate"));
                break;
            case "highPrice":
                pageable = PageRequest.of(realPage, 9, new Sort(Sort.Direction.DESC, "price", "createdDate"));
                break;
            case "lowPrice":
                pageable = PageRequest.of(realPage, 9, new Sort(Sort.Direction.ASC, "price", "createdDate"));
                break;
            case "highSell":
                pageable = PageRequest.of(realPage, 9, new Sort(Sort.Direction.DESC, "purchaseCount", "createdDate"));
                break;
            case "lowSell":
                pageable = PageRequest.of(realPage, 9, new Sort(Sort.Direction.ASC, "purchaseCount", "createdDate"));
                break;
            default:
                throw new NoValidProductSortException("유효하지 않은 상품 정렬입니다.");
        }
        return pageable;
    }

    private HashMap<String, Object> getResultMap(PageImpl<ProductResponseDto> products) {

        PagingDto questionPagingDto = new PagingDto();
        questionPagingDto.setPagingInfo(products);

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("productList", products);
        resultMap.put("productPagingDto", questionPagingDto);

        // PageImpl 객체를 반환
        return resultMap;
    }

    public List<ProductResponseDto.MainProductResponseDto> getBestProductList() {

        List<Product> bestProducts = productRepository.findTop10ByOrderByPurchaseCountDesc();

        List<ProductResponseDto.MainProductResponseDto> bestProductResponseList = new ArrayList<>();

        for (Product product : bestProducts) {
            bestProductResponseList.add(product.toMainProductResponseDto());
        }

        return bestProductResponseList;
    }

    public List<ProductResponseDto.MainProductResponseDto> getNewProductList() {

        List<Product> newProducts = productRepository.findTop8ByOrderByCreatedDateDesc();

        List<ProductResponseDto.MainProductResponseDto> newProductResponseList = new ArrayList<>();

        for (Product product : newProducts) {
            newProductResponseList.add(product.toMainProductResponseDto());
        }

        return newProductResponseList;
    }

    public String initReview(Long productId) {

        Optional<Product> productOpt = productRepository.findById(productId);

        if (!productOpt.isPresent())
            throw new NotExistProductException("존재하지 않는 상품입니다.");

        return productOpt.get().getProductNm();
    }
}

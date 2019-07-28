package com.shoppingmall.service;

import com.shoppingmall.domain.Product;
import com.shoppingmall.domain.ProductCat;
import com.shoppingmall.dto.PagingDto;
import com.shoppingmall.dto.ProductResponseDto;
import com.shoppingmall.repository.CategoryRepository;
import com.shoppingmall.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

        PageImpl<ProductResponseDto> products = new PageImpl(productResponseDtoList, pageable, productList.getTotalElements());
        
        PagingDto questionPagingDto = new PagingDto();
        questionPagingDto.setPagingInfo(products);

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("productList", products);
        resultMap.put("productPagingDto", questionPagingDto);

        // PageImpl 객체를 반환
        return resultMap;
    }
}

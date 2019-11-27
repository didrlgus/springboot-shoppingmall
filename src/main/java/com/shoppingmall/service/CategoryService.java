package com.shoppingmall.service;

import com.shoppingmall.domain.ProductCat;
import com.shoppingmall.dto.CategoryRequestDto;
import com.shoppingmall.dto.CategoryResponseDto;
import com.shoppingmall.exception.CatCdException;
import com.shoppingmall.exception.NotExistCategoryException;
import com.shoppingmall.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class CategoryService {

    private CategoryRepository categoryRepository;

    public HashMap<String, Object> getCategoryList() {

        HashMap<String, Object> resultMap = new HashMap<>();

        resultMap.put("mainCatList", categoryRepository.findAllByUseYn('Y'));
        resultMap.put("adminCatList", categoryRepository.findAll());

        return resultMap;
    }

    public String addFirstCategory(CategoryRequestDto.firstCategory firstCategory) {

        List<ProductCat> firstCategoryList = categoryRepository.findAllByCatLvOrderByCatCdDesc(1);

        String catCdOfFinalBigCategory = firstCategoryList.get(0).getCatCd();

        categoryRepository.save(ProductCat.builder()
                .catCd(makeFirstCatCd(catCdOfFinalBigCategory))
                .catLv(1)
                .catNm(firstCategory.getCatNm())
                .useYn(firstCategory.getUseYn())
                .build());

        return "1차 카테고리가 등록 되었습니다.";
    }

    public String addSecondCategory(CategoryRequestDto.secondCategory secondCategory) {
        String upprCatCd = secondCategory.getUpprCatCd();

        List<ProductCat> secondCategoryList = categoryRepository.findAllByUpprCatCdOrderByCatCdDesc(upprCatCd);

        String catCdOfNewSmallCategory = "";

        if (secondCategoryList.isEmpty()) {
            catCdOfNewSmallCategory = makeSecondCatCd(upprCatCd);
        } else {
            String catCdOfFinalSmallCategory = secondCategoryList.get(0).getCatCd();

            catCdOfNewSmallCategory = makeSecondCatCd(catCdOfFinalSmallCategory);
        }

        categoryRepository.save(ProductCat.builder()
                .catCd(catCdOfNewSmallCategory)
                .catLv(2)
                .catNm(secondCategory.getCatNm())
                .upprCatCd(upprCatCd)
                .cnntUrl("/productList")
                .useYn(secondCategory.getUseYn())
                .build());

        return "2차 카테고리가 등록 되었습니다.";
    }

    // 카테고리 상세
    @Transactional
    public ResponseEntity<?> getDetailOfCategory(Long id) {
        Optional<ProductCat> categoryOpt = categoryRepository.findById(id);

        if (!categoryOpt.isPresent())
            throw new NotExistCategoryException("존재하지 않는 카테고리 입니다.");

        ProductCat category = categoryOpt.get();

        if (category.getCatLv() == 1) {
            return ResponseEntity.ok().body(CategoryResponseDto.FirstCategory.builder()
                    .id(category.getId())
                    .catNm(category.getCatNm())
                    .useYn(category.getUseYn())
                    .build());
        } else {
            return ResponseEntity.ok().body(CategoryResponseDto.SecondCategory.builder()
                    .id(category.getId())
                    .catNm(category.getCatNm())
                    .upprCatNm(categoryRepository.findByCatCd(category.getUpprCatCd()).getCatNm())
                    .useYn(category.getUseYn())
                    .build());
        }
    }

    // 카테고리 수정
    public String updateCategory(Long id, CategoryRequestDto categoryDto) {
        Optional<ProductCat> categoryOpt = categoryRepository.findById(id);

        if (!categoryOpt.isPresent())
            throw new NotExistCategoryException("존재하지 않는 카테고리 입니다.");

        ProductCat category = categoryOpt.get();
        category.setCatNm(categoryDto.getCatNm());
        category.setUseYn(categoryDto.getUseYn());

        categoryRepository.save(category);

        return "카테고리가 수정되었습니다.";
    }

    // 2차 카테고리 리스트 조회
    public List<CategoryResponseDto.SecondCategory> getSecondCategoryList(String firstCatCd) {

        List<ProductCat> secondCategoryList = categoryRepository.findAllByUpprCatCd(firstCatCd);

        List<CategoryResponseDto.SecondCategory> secondCategoryDtoList = new ArrayList<>();

        for (ProductCat productCat : secondCategoryList) {
            secondCategoryDtoList.add(productCat.toSecondCategoryDto());
        }

        return secondCategoryDtoList;
    }

    private String makeFirstCatCd(String catCdOfFinalBigCategory) {
        String catCdStr = catCdOfFinalBigCategory.split("C")[1].split("000")[0];

        int catCdInt = Integer.parseInt(catCdStr);

        int newCatCdInt = catCdInt + 1;

        if (newCatCdInt >= 1000) {
            throw new CatCdException("더 이상 상위 카테고리를 추가할 수 없습니다.");
        }

        StringBuilder newCatCdSb = new StringBuilder();
        newCatCdSb.append(newCatCdInt);

        while(newCatCdSb.toString().length() < 3) {
            newCatCdSb = newCatCdSb.append("0");
        }

        newCatCdSb = newCatCdSb.append("C").reverse().append("000");

        return newCatCdSb.toString();
    }

    private String makeSecondCatCd(String catCd) {

        int newCatCdInt = Integer.parseInt(catCd.split("C")[1].substring(3)) + 1;

        if (newCatCdInt >= 1000) {
            throw new CatCdException("더 이상 하위 카테고리를 추가할 수 없습니다.");
        }

        StringBuilder sb = new StringBuilder();
        sb.append(catCd.substring(0, 3));
        sb.append(Integer.parseInt(catCd.split("C")[1]) + 1);
        catCd = sb.toString();

        return catCd;
    }


}

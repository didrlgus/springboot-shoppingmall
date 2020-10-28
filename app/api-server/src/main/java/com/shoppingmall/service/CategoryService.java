package com.shoppingmall.service;

import com.shoppingmall.domain.enums.Role;
import com.shoppingmall.domain.productCat.CategoryRepository;
import com.shoppingmall.domain.productCat.ProductCat;
import com.shoppingmall.dto.CategoryRequestDto;
import com.shoppingmall.dto.CategoryResponseDto;
import com.shoppingmall.exception.CatCdException;
import com.shoppingmall.exception.NotExistCategoryException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.shoppingmall.common.RedisKeyUtils.CATEGORY_LIST_KEY;

@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ValueOperations<String, Object> valueOperations;

    /**
     * 모든 카테고리 조회, 캐싱된 데이터 조회 (memory I/O)
     */
    @SuppressWarnings("unchecked")
    public HashMap<String, Object> getCategoryList() {

        return (HashMap<String, Object>) valueOperations.get(CATEGORY_LIST_KEY);
    }

    /**
     * 상위 카테고리 추가, 추가 후 캐시 업데이트
     */
    public String addFirstCategory(CategoryRequestDto.firstCategory firstCategory) {
        // Mysql에 추가 (File i/o)
        saveFirstCategory(firstCategory);

        // 상품 카테고리 캐싱 (Memory i/o)
        setCategoryCaching();

        return "1차 카테고리가 등록 되었습니다.";
    }

    /**
     * 하위 카테고리 추가, 추가 후 캐시 업데이트
     */
    public String addSecondCategory(CategoryRequestDto.secondCategory secondCategory) {
        // Mysql에 추가 (File i/o)
        saveSecondCategory(secondCategory);

        // 상품 카테고리 캐싱 (Memory i/o)
        setCategoryCaching();

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

    /**
     * 카테고리 수정 시, 캐시 업데이트
     */
    public String updateCategory(Long id, CategoryRequestDto categoryDto) {
        ProductCat category = categoryRepository.findById(id).orElseThrow(()
                -> new NotExistCategoryException("존재하지 않는 카테고리 입니다."));

        category.setCatNm(categoryDto.getCatNm());
        category.setUseYn(categoryDto.getUseYn());

        categoryRepository.save(category);

        // 캐시 업데이트
        setCategoryCaching();

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

    public HashMap<String, Object> getAdminCategoryList() {
        HashMap<String, Object> resultMap = new HashMap<>();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(String.valueOf(authentication.getAuthorities()).contains(Role.ADMIN.getKey())) {
            List<ProductCat> allCategoryList = categoryRepository.findAll();

            resultMap.put("adminCatList", allCategoryList);
            resultMap.put("mainCatList", allCategoryList.stream().
                    filter(productCat -> productCat.getUseYn() == 'Y').collect(Collectors.toList()));
        }

        return resultMap;
    }

    private void saveFirstCategory(CategoryRequestDto.firstCategory firstCategory) {
        List<ProductCat> firstCategoryList = categoryRepository.findAllByCatLvOrderByCatCdDesc(1);

        String catCdOfFinalBigCategory;

        if(firstCategoryList.isEmpty()) {
            catCdOfFinalBigCategory = "C010000";
        } else {
            catCdOfFinalBigCategory = makeFirstCatCd(firstCategoryList.get(0).getCatCd());
        }

        categoryRepository.save(ProductCat.builder()
                .catCd(catCdOfFinalBigCategory)
                .catLv(1)
                .catNm(firstCategory.getCatNm())
                .useYn(firstCategory.getUseYn())
                .build());
    }

    private void saveSecondCategory(CategoryRequestDto.secondCategory secondCategory) {
        String upprCatCd = secondCategory.getUpprCatCd();

        List<ProductCat> secondCategoryList = categoryRepository.findAllByUpprCatCdOrderByCatCdDesc(upprCatCd);

        String catCdOfNewSmallCategory;

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
            newCatCdSb.append("0");
        }

        newCatCdSb.append("C").reverse().append("000");

        return newCatCdSb.toString();
    }

    private String makeSecondCatCd(String catCd) {

        int newCatCdInt = Integer.parseInt(catCd.split("C")[1].substring(3)) + 1;

        if (newCatCdInt >= 1000) {
            throw new CatCdException("더 이상 하위 카테고리를 추가할 수 없습니다.");
        }

        catCd = catCd.substring(0, 3) + (Integer.parseInt(catCd.split("C")[1]) + 1);

        return catCd;
    }

    /**
     * 카테고리 추가 시 캐시 업데이트
     */
    private void setCategoryCaching() {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("mainCatList", categoryRepository.findAllByUseYn('Y'));

        valueOperations.set(CATEGORY_LIST_KEY, resultMap);
    }

}

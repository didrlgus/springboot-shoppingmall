package com.shoppingmall.service;

import com.shoppingmall.domain.product.Product;
import com.shoppingmall.domain.product.ProductRepository;
import com.shoppingmall.domain.productDisPrc.ProductDisPrc;
import com.shoppingmall.domain.productDisPrc.ProductDisPrcRepository;
import com.shoppingmall.dto.ProductDisPrcRequestDto;
import com.shoppingmall.dto.ProductDisPrcResponseDto;
import com.shoppingmall.exception.NotExistProductDisPrcException;
import com.shoppingmall.exception.NotExistProductException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductDiscountService {

    private final ProductRepository productRepository;
    private final ProductDisPrcRepository productDisPrcRepository;

    @Transactional
    public HashMap<String, Object> getDiscountList(Long id) {

        Optional<Product> productOpt = productRepository.findById(id);

        if (!productOpt.isPresent())
            throw new NotExistProductException("존재하지 않는 상품입니다.");

        Product product = productOpt.get();

        List<ProductDisPrc> productDisPrcList = productDisPrcRepository.findByProductId(id);

        int productDisPrcListSize = productDisPrcList.size();

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("productNm", product.getProductNm());

        if (productDisPrcListSize > 0) {
            // 현재 할인 적용 데이터
            List<ProductDisPrc> applyDiscountProductList = productDisPrcList.stream()
                    .filter(productDisPrc -> LocalDateTime.now().isAfter(productDisPrc.getStartDt())
                            && LocalDateTime.now().isBefore(productDisPrc.getEndDt()))
                    .sorted().limit(1).collect(Collectors.toList());

            Long applyDiscountProductId = 0L;

            // 현재 할인 적용 데이터가 있을 경우
            if (applyDiscountProductList.size() > 0) {
                ProductDisPrc applyDiscountProduct = applyDiscountProductList.get(0);
                ProductDisPrcResponseDto applyDiscountProductResponseDto = applyDiscountProduct.toResponseDto();

                applyDiscountProductId = applyDiscountProductResponseDto.getId();

                resultMap.put("applyDiscountProduct", applyDiscountProductResponseDto);
            }

            // 현재 적용된 할인 데이터 외의 다른 할인 데이터가 있을 경우
            // 현재 할인이 적용되지 않는 모든 할인 리스트 (스트림의 map을 통해 ResponseDto로 변환 후 ResponseDto로 반환)
            // 현재 할인이 적용된 데이터를 제외하고 조회
            Long finalApplyDiscountProductId = applyDiscountProductId;

            List<ProductDisPrcResponseDto> NoApplyDiscountProductResponseDtoList
                    = productDisPrcList.stream().map(ProductDisPrc::toResponseDto)
                    .filter(productDisPrcResponseDto -> !productDisPrcResponseDto.getId().equals(finalApplyDiscountProductId))
                    .collect(Collectors.toList());

            resultMap.put("NoApplyDiscountProductList", NoApplyDiscountProductResponseDtoList);
        }

        return resultMap;
    }

    // 할인 리스트 추가
    public String addProductDiscount(ProductDisPrcRequestDto productDisPrcRequestDto) {
        Optional<Product> productOpt = productRepository.findById(productDisPrcRequestDto.getProductId());

        if (!productOpt.isPresent())
            throw new NotExistProductException("존재하지 않는 상품 입니다.");

        productDisPrcRepository.save(ProductDisPrc.builder()
                .product(productOpt.get())
                .startDt(productDisPrcRequestDto.getStartDt().minusHours(15))
                .endDt(productDisPrcRequestDto.getEndDt().plusHours(8).plusMinutes(59).plusSeconds(59))
                .rateYn('Y')
                .disPrc(productDisPrcRequestDto.getDisPrc())
                .build());

        return "할인 리스트가 추가되었습니다.";
    }

    // 할인 리스트 삭제
    public String deleteProductDiscount(Long id) {
        Optional<ProductDisPrc> productDisPrcOpt = productDisPrcRepository.findById(id);

        if (!productDisPrcOpt.isPresent())
            throw new NotExistProductDisPrcException("존재하지 않는 할인 리스트 입니다.");

        productDisPrcRepository.delete(productDisPrcOpt.get());

        return "할인 리스트가 삭제되었습니다.";
    }
}

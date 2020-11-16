package com.shoppingmall.service;

import com.shoppingmall.domain.cart.Cart;
import com.shoppingmall.domain.cart.CartRepository;
import com.shoppingmall.domain.product.Product;
import com.shoppingmall.domain.productPurchaseCount.ProductPurchaseCount;
import com.shoppingmall.domain.productPurchaseCount.ProductPurchaseCountRepository;
import com.shoppingmall.dto.PaymentRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductPurchaseCountService {

    private final CartRepository cartRepository;
    private final ProductPurchaseCountRepository productPurchaseCountRepository;
    @Value("${spring.kafka.consumer.client-id}")
    private String clientId;

    /**
     * 결제 완료 후 상품 구매 수량 업데이트
     */
    @Transactional
    public void updateProductPurchaseCount(PaymentRequestDto.Success message) {
        List<Long> cartIdList = message.getCartIdList();

        List<Cart> cartList = cartRepository.findAllById(cartIdList);

        Map<Long, Integer> productPurchaseCountMap = getProductPurchaseCountMap(cartList);

        List<ProductPurchaseCount> productPurchaseCountList = getProductPurchaseCountList(productPurchaseCountMap);

        productPurchaseCountRepository.saveAll(productPurchaseCountList);

        log.info("[ProductService.updateProductPurchaseCount] 상품 구매 수량 업데이트 완료");
    }

    private Map<Long, Integer> getProductPurchaseCountMap(List<Cart> cartList) {
        Map<Long, Integer> productPurchaseCountMap = new HashMap<>();

        for(Cart cart : cartList) {
            Product product = cart.getProduct();
            Long productId = product.getId();

            if(productPurchaseCountMap.containsKey(productId)) {
                Integer productPurchaseCount = productPurchaseCountMap.get(productId);
                productPurchaseCountMap.replace(productId, ++productPurchaseCount);
                continue;
            }
            productPurchaseCountMap.put(productId, 1);
        }

        return productPurchaseCountMap;
    }

    private List<ProductPurchaseCount> getProductPurchaseCountList(Map<Long, Integer> productPurchaseCountMap) {
        List<ProductPurchaseCount> productPurchaseCountList = new ArrayList<>();

        LocalDateTime currentDateTime = LocalDateTime.now();

        productPurchaseCountMap.forEach((productId, count)
                -> productPurchaseCountList.add(getProductPurchaseCount(currentDateTime, productId, count)));

        return productPurchaseCountList;
    }

    private ProductPurchaseCount getProductPurchaseCount(LocalDateTime currentDateTime, Long productId,
                                                         Integer count) {
        return ProductPurchaseCount.builder()
                .dateTime(currentDateTime)
                .clientId(clientId)
                .productId(productId)
                .count(count)
                .build();
    }
}

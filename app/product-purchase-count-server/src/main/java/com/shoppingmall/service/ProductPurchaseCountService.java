package com.shoppingmall.service;

import com.shoppingmall.domain.cart.Cart;
import com.shoppingmall.domain.cart.CartRepository;
import com.shoppingmall.domain.product.Product;
import com.shoppingmall.domain.product.ProductRepository;
import com.shoppingmall.dto.PaymentRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductPurchaseCountService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    /**
     * 결제 완료 후 상품 재고 업데이트
     */
    @Transactional
    public void updateProductPurchaseCount(PaymentRequestDto.Success message) {
        List<Long> cartIdList = message.getCartIdList();

        List<Cart> cartList = cartRepository.findAllById(cartIdList);

        List<Product> productList = new ArrayList<>();

        for(Cart cart : cartList) {
            Product product = cart.getProduct();
            product.setPurchaseCount(product.getPurchaseCount() + cart.getProductCount());
            product.setTotalCount(product.getTotalCount() - cart.getProductCount());

            productList.add(cart.getProduct());
        }

        productRepository.saveAll(productList);

        log.info("[ProductService.updateProductPurchaseCount] 상품 구매 수량 업데이트 완료");
    }

}

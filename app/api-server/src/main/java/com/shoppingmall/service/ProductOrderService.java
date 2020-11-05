package com.shoppingmall.service;

import com.shoppingmall.domain.cart.Cart;
import com.shoppingmall.domain.cart.CartRepository;
import com.shoppingmall.domain.enums.OrderStatus;
import com.shoppingmall.domain.productOrder.ProductOrder;
import com.shoppingmall.domain.productOrder.ProductOrderRepository;
import com.shoppingmall.domain.user.User;
import com.shoppingmall.domain.user.UserRepository;
import com.shoppingmall.dto.PagingDto;
import com.shoppingmall.dto.PaymentRequestDto;
import com.shoppingmall.dto.ProductOrderResponseDto;
import com.shoppingmall.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductOrderService {

    private final UserRepository userRepository;
    private final ProductOrderRepository productOrderRepository;
    private final CartRepository cartRepository;

    @Transactional
    public void makeOrder(PaymentRequestDto.Success message) {

        // 주문 생성
        ProductOrder productOrder = productOrderRepository.save(getProductOrder(message));

        // 장바구니에 연관된 주문데이터 설정
        setProductOrderOfCart(message.getCartIdList(), productOrder);

        log.info("[ProductOrderService.makeOrder] 주문 생성 완료");
    }

    public ProductOrderResponseDto getOrderDetails(Long orderId) {

        Optional<ProductOrder> orderOpt = productOrderRepository.findById(orderId);

        if (!orderOpt.isPresent())
            throw new NotExistOrderException("존재하지 않는 주문입니다.");

        return orderOpt.get().toResponseDto();
    }

    public HashMap<String, Object> getAllOrder(UUID userId, int page) {
        int realPage = (page == 0) ? 0 : (page - 1);
        PageRequest pageable = PageRequest.of(realPage, 5);

        Page<ProductOrder> productOrderPage = productOrderRepository.findAllByUserIdOrderByCreatedDateDesc(userId, pageable);

        if (productOrderPage.getTotalElements() > 0) {
            List<ProductOrderResponseDto> productOrderResponseDtoList = new ArrayList<>();

            for (ProductOrder productOrder : productOrderPage) {
                productOrderResponseDtoList.add(productOrder.toResponseDto());
            }

            PageImpl<ProductOrderResponseDto> productOrderResponseDtos
                    = new PageImpl<>(productOrderResponseDtoList, pageable, productOrderPage.getTotalElements());

            PagingDto productOrderPagingDto = new PagingDto();
            productOrderPagingDto.setPagingInfo(productOrderResponseDtos);

            HashMap<String, Object> resultMap = new HashMap<>();
            resultMap.put("productOrderList", productOrderResponseDtos);
            resultMap.put("productOrderPagingDto", productOrderPagingDto);

            return resultMap;
        }

        return null;
    }

    private User getUser(PaymentRequestDto.Success message) {
        return userRepository.findById(message.getUserId()).orElseThrow(() -> new NotExistUserException("존재하지 않는 유저 입니다."));
    }

    private ProductOrder getProductOrder(PaymentRequestDto.Success message) {

        User user = getUser(message);

        return ProductOrder.builder()
                .user(user)
                .orderNumber(message.getOrderNumber())
                .orderName(message.getOrderName())
                .amount(message.getAmount())
                .deliveryMessage(message.getDeliveryMessage())
                .address(message.getAddress())
                .orderStatus(OrderStatus.COMPLETE)
                .refundState('N')
                .build();
    }

    private void setProductOrderOfCart(List<Long> cartIdList, ProductOrder productOrder) {
        List<Cart> cartList = cartRepository.findAllById(cartIdList);

        for(Cart cart : cartList) {
            cart.setProductOrder(productOrder);
            cart.setUseYn('N');
        }
    }
}

package com.shoppingmall.service;

import com.shoppingmall.domain.cart.Cart;
import com.shoppingmall.domain.cart.CartRepository;
import com.shoppingmall.domain.enums.OrderStatus;
import com.shoppingmall.domain.productOrder.ProductOrder;
import com.shoppingmall.domain.productOrder.ProductOrderRepository;
import com.shoppingmall.domain.user.User;
import com.shoppingmall.domain.user.UserRepository;
import com.shoppingmall.dto.PaymentRequestDto;
import com.shoppingmall.exception.NotExistUserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {

    private final ProductOrderRepository productOrderRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    /**
     * 결제 성공 이후 주문서 생성
     */
    @Transactional
    public void makeOrder(PaymentRequestDto.Success message) {
        // 주문 생성
        ProductOrder productOrder = productOrderRepository.save(getProductOrder(message));

        // 장바구니에 연관된 주문데이터 설정
        setProductOrderOfCart(message.getCartIdList(), productOrder);

        log.info("[ProductOrderService.makeOrder] 주문 생성 완료");
    }

    /**
     * 결제 성공 이후 적립금 update
     */
    @Transactional
    public void updateSavings(PaymentRequestDto.Success message) {
        int useSavings = message.getUseSavings();

        // 총 결제액의 3% 적립
        int addSavings = (int)((((float) 3 / (float)100) * message.getAmount()));

        User user = userRepository.findById(message.getUserId()).orElseThrow(()
                -> new NotExistUserException("존재하지 않는 유저입니다."));

        user.setSavings(user.getSavings() - useSavings + addSavings);

        userRepository.save(user);

        log.info("[UserService.updateSavings] 적립금 업데이트 완료");
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

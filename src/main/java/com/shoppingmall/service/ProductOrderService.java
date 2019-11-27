package com.shoppingmall.service;

import com.google.gson.internal.LinkedTreeMap;
import com.shoppingmall.common.ImpProperties;
import com.shoppingmall.common.JsonUtil;
import com.shoppingmall.domain.Cart;
import com.shoppingmall.domain.NormalUser;
import com.shoppingmall.domain.Product;
import com.shoppingmall.domain.ProductOrder;
import com.shoppingmall.domain.enums.OrderStatus;
import com.shoppingmall.dto.PagingDto;
import com.shoppingmall.dto.ProductOrderRequestDto;
import com.shoppingmall.dto.ProductOrderResponseDto;
import com.shoppingmall.exception.*;
import com.shoppingmall.repository.CartRepository;
import com.shoppingmall.repository.NormalUserRepository;
import com.shoppingmall.repository.ProductOrderRepository;
import com.shoppingmall.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductOrderService {

    private final CartRepository cartRepository;
    private final NormalUserRepository normalUserRepository;
    private final ProductOrderRepository productOrderRepository;
    private final ProductRepository productRepository;
    private final RestTemplate restTemplate;
    private final ImpProperties impProperties;

    /*@Value("${custom.imp.key}")
    private String imp_key;
    @Value("${custom.imp.secret}")
    private String imp_secret;*/

    @Transactional
    public void makeOrder(ProductOrderRequestDto productOrderRequestDto) {
        String getTokenUrl = "https://api.iamport.kr/users/getToken";

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("imp_key", impProperties.getKey());
        requestMap.put("imp_secret", impProperties.getSecret());

        ResponseEntity<String> responseAccessToken = restTemplate.postForEntity(getTokenUrl, requestMap, String.class);

        Map<String, Object> responseMap;

        if(responseAccessToken.getStatusCode() == HttpStatus.OK) {
            responseMap = JsonUtil.JsonToMap(JsonUtil.JsonToMap(responseAccessToken.getBody()).get("response").toString());
        } else {
            throw new PaymentsException("결제에러 입니다.");
        }

        String impAccessToken = (String) responseMap.get("access_token");
        String impUid = productOrderRequestDto.getImpUid();
        String getPaymentInfoUrl = "https://api.iamport.kr/payments/" + impUid;

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/json");
        headers.add("Authorization", "Bearer "+impAccessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);

        Map<String, Object> responsePaymentInfoMap;

        ResponseEntity<String> responsePaymentInfo = restTemplate.postForEntity(getPaymentInfoUrl, request, String.class);

        if (responsePaymentInfo.getStatusCode() == HttpStatus.OK) {
            responsePaymentInfoMap = JsonUtil.JsonToMap(responsePaymentInfo.getBody());
        } else {
            throw new PaymentsException("결제에러 입니다.");
        }

        Map<String, Object> response = (LinkedTreeMap<String, Object>) responsePaymentInfoMap.get("response");
        String paymentStatus = (String) response.get("status");


        if(!paymentStatus.equals("paid")) throw new PaymentsException("결제에러 입니다.");

        // 해당 유저의 장바구니 조회
        List<Long> cartIdList = productOrderRequestDto.getCartIdList();

        Optional<Cart> cartOpt = cartRepository.findById(cartIdList.get(0));

        if (!cartOpt.isPresent()) {
            throw new NotExistCartException("존재하지 않는 장바구니 입니다.");
        }

        Cart cart = cartOpt.get();
        Long userId = cart.getNormalUser().getId();

        Optional<NormalUser> userOpt = normalUserRepository.findById(userId);

        if (!userOpt.isPresent()) {
            throw new NotExistUserException("존재하지 않는 유저 입니다.");
        }

        NormalUser user = userOpt.get();

        ProductOrder productOrder = productOrderRepository.save(ProductOrder.builder()
                .normalUser(user)
                .orderNumber(productOrderRequestDto.getOrderNumber())
                .orderName(productOrderRequestDto.getOrderName())
                .amount(productOrderRequestDto.getAmount())
                .deliveryMessage(productOrderRequestDto.getDeliveryMessage())
                .address(productOrderRequestDto.getAddress())
                .orderStatus(OrderStatus.COMPLETE)
                .refundState('N')
                .build());

        List<HashMap<String, Object>> productMapList = new ArrayList<>();

        for (Long cartId : cartIdList) {
            cartOpt = cartRepository.findById(cartId);

            if(cartOpt.isPresent()) {
                // 사용한 장바구니 비활성화
                cart = cartOpt.get();
                cart.setProductOrder(productOrder);
                cart.setUseYn('N');

                HashMap<String, Object> productMap = new HashMap<>();
                productMap.put("product", cart.getProduct());
                productMap.put("productCount", cart.getProductCount());
                productMapList.add(productMap);

                cartRepository.save(cart);
            } else {
                throw new NotExistCartException("존재하지 않는 장바구니 입니다.");
            }
        }

        // 상품의 재고 수정
        for (HashMap<String, Object> productMap : productMapList) {
            Product product = (Product) productMap.get("product");
            Integer productCount = (Integer) productMap.get("productCount");

            product.setPurchaseCount(product.getPurchaseCount() + productCount);
            product.setLimitCount(product.getLimitCount() - productCount);
            product.setTotalCount(product.getTotalCount() - productCount);
            productRepository.save(product);
        }

        // 적립금 수정
        if (user.getSavings() < productOrderRequestDto.getUseSavings()) {
            throw new SavingsException("갖고 있는 적립금 보다 많은 적립금을 사용할 수 없습니다.");
        }
        // 추가될 적립금 (결제금액의 3%)
        int addSavings = (int)((((float) 3 / (float)100) * productOrderRequestDto.getAmount()));

        user.setSavings(user.getSavings() - productOrderRequestDto.getUseSavings() + addSavings);
        normalUserRepository.save(user);
    }

    public ProductOrderResponseDto getOrderDetails(Long orderId) {

        Optional<ProductOrder> orderOpt = productOrderRepository.findById(orderId);

        if (!orderOpt.isPresent())
            throw new NotExistOrderException("존재하지 않는 주문입니다.");

        return orderOpt.get().toResponseDto();
    }

    public HashMap<String, Object> getAllOrder(Long userId, int page, Pageable pageable) {
        int realPage = (page == 0) ? 0 : (page - 1);
        pageable = PageRequest.of(realPage, 5);

        Page<ProductOrder> productOrderPage = productOrderRepository.findAllByNormalUserIdOrderByCreatedDateDesc(userId, pageable);

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
}

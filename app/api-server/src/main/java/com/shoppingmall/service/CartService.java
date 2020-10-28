package com.shoppingmall.service;

import com.shoppingmall.domain.cart.Cart;
import com.shoppingmall.domain.cart.CartRepository;
import com.shoppingmall.domain.product.Product;
import com.shoppingmall.domain.product.ProductRepository;
import com.shoppingmall.domain.productDisPrc.ProductDisPrc;
import com.shoppingmall.domain.user.User;
import com.shoppingmall.domain.user.UserRepository;
import com.shoppingmall.dto.CartRequestDto;
import com.shoppingmall.dto.CartResponseDto;
import com.shoppingmall.dto.PagingDto;
import com.shoppingmall.exception.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class CartService {
    private UserRepository userRepository;
    private ProductRepository productRepository;
    private CartRepository cartRepository;
    private JobLauncher jobLauncher;
    private Job job;

    @Transactional
    public void makeCart(CartRequestDto cartRequestDto) {
        User user = userRepository.findById(cartRequestDto.getUserId()).orElseThrow(()
                -> new NotExistUserException("존재하지 않는 유저입니다."));

        Optional<Product> productOpt = productRepository.findById(cartRequestDto.getProductId());

        if (!productOpt.isPresent())
            throw new NotExistProductException("존재하지 않는 상품입니다.");

        Product product = productOpt.get();

        if (product.getLimitCount() < cartRequestDto.getProductCount())
            throw new ProductLimitCountException("재고가 없습니다.");

        cartRepository.save(Cart.builder()
                .user(user)
                .product(product)
                .productCount(cartRequestDto.getProductCount())
                .useYn('Y')
                .build());
    }

    @Transactional
    public HashMap<String, Object> getCartList(UUID userId, int page, Pageable pageable) {
        int realPage = page - 1;
        pageable = PageRequest.of(realPage, 5);

        Page<Cart> cartList = cartRepository.findAllByUserIdAndUseYnOrderByCreatedDateDesc(userId, 'Y', pageable);

        if (cartList.getTotalElements() > 0) {
            List<CartResponseDto> cartResponseDtoList = new ArrayList<>();

            for (Cart cart : cartList) {

                cartResponseDtoList.add(cart.toResponseDto(getDisPrice(cart)));
            }

            PageImpl<CartResponseDto> cartLists = new PageImpl<>(cartResponseDtoList, pageable, cartList.getTotalElements());

            PagingDto cartPagingDto = new PagingDto();
            cartPagingDto.setPagingInfo(cartLists);

            List<Cart> carts = cartRepository.findAllByUserIdAndUseYnOrderByCreatedDateDesc(userId, 'Y');
            int checkoutPrice = 0;
            List<Long> cartIdList = new ArrayList<>();

            for (Cart cart : carts) {
                cartIdList.add(cart.getId());

                int salePrice = (int)((((float) 100 - (float) getDisPrice(cart)) / (float)100) * cart.getProduct().getPrice());

                checkoutPrice += salePrice * cart.getProductCount();
            }

            HashMap<String, Object> resultMap = new HashMap<>();
            resultMap.put("cartList", cartLists);
            resultMap.put("cartPagingDto", cartPagingDto);
            resultMap.put("checkoutPrice", checkoutPrice);
            resultMap.put("cartIdList", cartIdList);

            return resultMap;
        }
        return null;
    }

    @Transactional
    public void removeCart(Long id) {
        Optional<Cart> cartOpt = cartRepository.findById(id);

        if (!cartOpt.isPresent()) {
            throw new NotExistCartException("존재하지 않는 장바구니 입니다.");
        }

        cartRepository.delete(cartOpt.get());
    }

    @Transactional
    public int checkReviewAuthority(HashMap<String, Object> paramMap) {
        UUID userId = UUID.fromString(paramMap.get("userId").toString());
        Long productId = Long.parseLong(paramMap.get("productId").toString());

        List<Cart> cartList = cartRepository.findAllByUserIdAndProductId(userId, productId);

        if (cartList.size() > 0) {
            for (Cart cart : cartList) {
                if (cart.getProductOrder() != null) {
                    return 1;
                }
            }
        }

        throw new CheckReviewAuthorityException("해당상품 결제를 완료한 회원만 리뷰를 작성할 수 있습니다.");
    }

    @Transactional
    public int getDisPrice(Cart cart) {

        int disPrice = 0;

        if (cart.getProduct().getProductDisPrcList().size() > 0) {
            List<ProductDisPrc> disprcList
                    = cart.getProduct().getProductDisPrcList().stream().filter(productDisPrc -> LocalDateTime.now().isAfter(productDisPrc.getStartDt())
                    && LocalDateTime.now().isBefore(productDisPrc.getEndDt())).sorted().limit(1).collect(Collectors.toList());

            // getProductDisPrcList의 크기가 0보다 크다 하더라도 할인 기간에 적용되지 않는 리스트는 disprcList에 포함되지 않기 때문에 아래의 if조건을 걸어줘야 함
            if (disprcList.size() > 0)
                disPrice = disprcList.get(0).getDisPrc();
        }

        return disPrice;
    }

    // 매일 자정 배치를 작동시켜 7일이 지난 장바구니 데이터를 자동으로 비활성화
    @Scheduled(cron = "0 0 0 * * *")
    public void toInvalidityCart() throws Exception {

        log.info("Batch : 유효기간이 지난 장바구니 비활성화");

        Date nowDate = new Date();
        // job 파라미터 설정
        JobParameters jobParameters = new JobParametersBuilder().addDate("nowDate", nowDate).toJobParameters();
        // job 실행
        jobLauncher.run(job, jobParameters);
    }
}

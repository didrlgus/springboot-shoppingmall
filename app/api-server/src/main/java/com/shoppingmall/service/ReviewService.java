package com.shoppingmall.service;

import com.amazonaws.services.s3.AmazonS3;
import com.shoppingmall.common.AWSS3Utils;
import com.shoppingmall.common.UploadFileUtils;
import com.shoppingmall.domain.product.Product;
import com.shoppingmall.domain.product.ProductRepository;
import com.shoppingmall.domain.review.Review;
import com.shoppingmall.domain.review.ReviewRepository;
import com.shoppingmall.domain.user.User;
import com.shoppingmall.domain.user.UserRepository;
import com.shoppingmall.dto.PagingDto;
import com.shoppingmall.dto.ReviewRequestDto;
import com.shoppingmall.dto.ReviewResponseDto;
import com.shoppingmall.exception.NotExistProductException;
import com.shoppingmall.exception.NotExistReviewException;
import com.shoppingmall.exception.NotExistUserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewService {

    private final AWSS3Utils awss3Utils;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;

    public String initReview(Long productId) {

        Product product = productRepository.findById(productId).orElseThrow(()
                -> new NotExistProductException("존재하지 않는 상품입니다."));

        return product.getProductNm();
    }

    public String uploadReviewImage(MultipartFile file, String dirName) throws IOException {
        // S3와 연결할 client 얻기
        AmazonS3 s3Client = awss3Utils.getS3Client();

        // S3에 저장할 파일 경로 얻기
        String saveFilePath = UploadFileUtils.getSaveFilePath(file, dirName);

        return awss3Utils.putObjectToS3AndGetUrl(s3Client, saveFilePath, file);
    }

    // 리뷰 추가 서비스
    @Transactional
    public void makeReview(ReviewRequestDto reviewRequestDto) {
        User user = userRepository.findById(reviewRequestDto.getUserId())
                .orElseThrow(() -> new NotExistUserException("존재하지 않는 유저입니다."));

        Product product = productRepository.findById(reviewRequestDto.getProductId())
                .orElseThrow(() -> new NotExistProductException("존재하지 않는 상품입니다."));

        reviewRepository.save(Review.builder()
                .user(user)
                .product(product)
                .title(reviewRequestDto.getTitle())
                .content(reviewRequestDto.getContent())
                .rate(reviewRequestDto.getRate())
                .build());

        // 상품 평점 세팅하기
        double reviewRateAvg = reviewRepository.getReviewRateAvg(product);

        // 1점당 20% 이므로 20을 곱함, 평균값 계산의 결과로 나온 소수점은 버림
        int productRateAvg = (int) (reviewRateAvg * 20);
        product.setRateAvg(productRateAvg);

        // 상품 평점 업데이트
        productRepository.save(product);
    }

    // 리뷰 리스트 조회
    public HashMap<String, Object> getReviewList(Long productId, int page) {
        int realPage = page - 1;
        Pageable pageable = PageRequest.of(realPage, 3);

        Page<Review> reviewPage = reviewRepository.findAllByProductIdOrderByCreatedDateDesc(productId, pageable);

        List<ReviewResponseDto> reviewResponseDtoList = new ArrayList<>();

        for (Review review : reviewPage) {
            reviewResponseDtoList.add(review.toResponseDto());
        }

        PageImpl<ReviewResponseDto> reviewResponseDtos
                = new PageImpl<>(reviewResponseDtoList, pageable, reviewPage.getTotalElements());

        PagingDto reviewPagingDto = new PagingDto();
        reviewPagingDto.setPagingInfo(reviewResponseDtos);

        // 평점 평균 조회해서 맵에 추가
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotExistProductException("존재하지 않는 상품입니다."));

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("reviewList", reviewResponseDtos);
        resultMap.put("reviewPagingDto", reviewPagingDto);
        resultMap.put("rateAvg", product.getRateAvg());

        return resultMap;
    }

    public ReviewResponseDto.ReviewDetailResponseDto getReviewDetail(Long id) {

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new NotExistReviewException("존재하지 않는 리뷰입니다."));

        return ReviewResponseDto.ReviewDetailResponseDto.builder()
                .content(review.getContent())
                .build();
    }
}

package com.shoppingmall.service;

import com.shoppingmall.common.FileUploadProperties;
import com.shoppingmall.common.UploadFileUtils;
import com.shoppingmall.domain.NormalUser;
import com.shoppingmall.domain.Product;
import com.shoppingmall.domain.Review;
import com.shoppingmall.domain.UploadFile;
import com.shoppingmall.dto.PagingDto;
import com.shoppingmall.dto.ReviewRequestDto;
import com.shoppingmall.dto.ReviewResponseDto;
import com.shoppingmall.exception.NotExistProductException;
import com.shoppingmall.exception.NotExistReviewException;
import com.shoppingmall.exception.NotExistUserException;
import com.shoppingmall.repository.NormalUserRepository;
import com.shoppingmall.repository.ProductRepository;
import com.shoppingmall.repository.ReviewRepository;
import com.shoppingmall.repository.UploadFileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ReviewService {

    private final Path rootLocation;

    @Autowired
    public ReviewService(FileUploadProperties prop) {
        this.rootLocation = Paths.get(prop.getUploadDir())
                .toAbsolutePath().normalize();
    }
    @Autowired
    private UploadFileRepository uploadFileRepository;
    @Autowired
    private NormalUserRepository normalUserRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    @Transactional
    public UploadFile uploadReviewImage(MultipartFile file) throws Exception {

        try {
            if (file.isEmpty()) {
                throw new Exception("Failed to store empty file " + file.getOriginalFilename());
            }

            String saveFileName = UploadFileUtils.fileSave(rootLocation.toString(), file);

            if (saveFileName.toCharArray()[0] == '/') {
                saveFileName = saveFileName.substring(1);
            }

            Resource resource = loadAsResource(saveFileName);

            UploadFile saveFile = new UploadFile();
            saveFile.setSaveFileName(saveFileName);
            saveFile.setFileName(file.getOriginalFilename());
            saveFile.setContentType(file.getContentType());
            saveFile.setFilePath(rootLocation.toString().replace(File.separatorChar, '/') + File.separator + saveFileName);
            saveFile.setSize(resource.contentLength());
            saveFile = uploadFileRepository.save(saveFile);

            return saveFile;
        } catch (IOException e) {
            throw new Exception("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    private Resource loadAsResource(String fileName) throws Exception {
        try {
            if (fileName.toCharArray()[0] == '/') {
                fileName = fileName.substring(1);
            }

            Path file = loadPath(fileName);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new Exception("Could not read file: " + fileName);
            }
        } catch (Exception e) {
            throw new Exception("Could not read file: " + fileName);
        }
    }

    private Path loadPath(String fileName) {
        return rootLocation.resolve(fileName);
    }

    // 리뷰 추가 서비스
    @Transactional
    public void makeReview(ReviewRequestDto reviewRequestDto) {
        Optional<NormalUser> userOpt = normalUserRepository.findById(reviewRequestDto.getUserId());

        if (!userOpt.isPresent())
            throw new NotExistUserException("존재하지 않는 유저입니다.");

        Optional<Product> productOpt = productRepository.findById(reviewRequestDto.getProductId());

        if (!productOpt.isPresent())
            throw new NotExistProductException("존재하지 않는 상품입니다.");


        Product product = productOpt.get();

        reviewRepository.save(Review.builder()
                .normalUser(userOpt.get())
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
        Optional<Product> productOpt = productRepository.findById(productId);

        if (!productOpt.isPresent())
            throw new NotExistProductException("존재하지 않는 상품입니다.");

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("reviewList", reviewResponseDtos);
        resultMap.put("reviewPagingDto", reviewPagingDto);
        resultMap.put("rateAvg", productOpt.get().getRateAvg());

        return resultMap;
    }

    public ReviewResponseDto.ReviewDetailResponseDto getReviewDetail(Long id) {

        Optional<Review> reviewOpt = reviewRepository.findById(id);

        if (!reviewOpt.isPresent())
            throw new NotExistReviewException("존재하지 않는 리뷰입니다.");

        return ReviewResponseDto.ReviewDetailResponseDto.builder()
                .content(reviewOpt.get().getContent())
                .build();
    }
}

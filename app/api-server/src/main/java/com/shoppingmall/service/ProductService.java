package com.shoppingmall.service;

import com.amazonaws.services.s3.AmazonS3;
import com.shoppingmall.common.AWSS3Utils;
import com.shoppingmall.common.UploadFileUtils;
import com.shoppingmall.domain.enums.ProductStatus;
import com.shoppingmall.domain.product.Product;
import com.shoppingmall.domain.product.ProductRepository;
import com.shoppingmall.domain.productDisPrc.ProductDisPrc;
import com.shoppingmall.domain.productDisPrc.ProductDisPrcRepository;
import com.shoppingmall.dto.PagingDto;
import com.shoppingmall.dto.ProductRequestDto;
import com.shoppingmall.dto.ProductResponseDto;
import com.shoppingmall.exception.NoValidProductSortException;
import com.shoppingmall.exception.NotExistProductException;
import com.shoppingmall.exception.ProductListException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.shoppingmall.common.RedisKeyUtils.BEST10_PRODUCT_LIST_KEY;
import static com.shoppingmall.common.RedisKeyUtils.NEW8_PRODUCT_LIST_KEY;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {

    private final AWSS3Utils awss3Utils;
    private final ProductRepository productRepository;
    private final ProductDisPrcRepository productDisPrcRepository;
    private final ZSetOperations<String, Object> zSetOperations;

    // 전체 상품 혹은 카테고리로 상품 조회
    public HashMap<String, Object> getProductList(String catCd, String sortCd, String saleCd, int page) throws Exception {
        int realPage = (page == 0) ? 0 : page - 1;
        Pageable pageable = PageRequest.of(realPage, 9, new Sort(Sort.Direction.DESC, "createdDate"));

        // 카테고리로 조회
        if(isNull(sortCd) && isNull(saleCd)) {
            return getResult(getProductsByCategory(catCd, pageable), pageable);
        }

        // 할인 상품 조회
        if(nonNull(saleCd) && isNull(catCd) && isNull(sortCd)) {
            return getSaleProductList(realPage);
        }

        // 정렬 기준으로 조회
        if(nonNull(sortCd) && nonNull(catCd) && isNull(saleCd)) {
            return getResult(getProductsByCatCdAndSortCd(catCd, sortCd, realPage), pageable);
        }

        throw new NoValidProductSortException("유효하지 않은 상품 조회 요청 파라미터 입니다.");
    }

    // 상품 상세
    public ProductResponseDto getProductDetails(Long id) {
        Product product = productRepository.findById(id).orElseThrow(()
                -> new NotExistProductException("존재하지 않는 상품입니다."));

        int disPrice = 0;
        if (product.getProductDisPrcList().size() > 0) {
            disPrice = getDisPrice(product);
        }

        return product.toResponseDto(disPrice);
    }

    /**
     * 인기 상위 10개의 상품 조회
     */
    public List<ProductResponseDto.MainProductResponseDto> getBestProductList() {
        // 레디스 캐시(메모리) I/O
        Set<Object> result = zSetOperations.reverseRange(BEST10_PRODUCT_LIST_KEY, 0, 9);

        if(isNull(result)) {
            return null;
        } else {
            return result.stream().map(el -> (ProductResponseDto.MainProductResponseDto) el).collect(Collectors.toList());
        }
    }

    /**
     * 최신 상위 8개 상품 조회
     */
    public List<ProductResponseDto.MainProductResponseDto> getNewProductList() {
        // 레디스 캐시(메모리) I/O
        Set<Object> result = zSetOperations.reverseRange(NEW8_PRODUCT_LIST_KEY, 0, 7);

        if(isNull(result)) {
            return null;
        } else {
            return result.stream().map(el -> (ProductResponseDto.MainProductResponseDto) el).collect(Collectors.toList());
        }
    }

    // 세일 중인 상품 리스트 얻기
    public HashMap<String, Object> getSaleProductList(int realPage) {
        Pageable pageable = PageRequest.of(realPage, 9, new Sort(Sort.Direction.DESC, "createdDate"));

        Page<Map<String, Object>> productPage = productDisPrcRepository.getSaleProductList(pageable);

        List<ProductResponseDto.SaleProductResponseDto> saleProductResponseDtoList = new ArrayList<>();

        for (Map<String, Object> map : productPage) {
            saleProductResponseDtoList.add(mapToDto(map));
        }

        PageImpl<ProductResponseDto.SaleProductResponseDto> saleProductResponseDtoPage
                = new PageImpl<>(saleProductResponseDtoList, pageable, productPage.getTotalElements());

        PagingDto saleProductPagingDto = new PagingDto();
        saleProductPagingDto.setPagingInfo(saleProductResponseDtoPage);

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("saleProductList", saleProductResponseDtoPage);
        resultMap.put("saleProductPagingDto", saleProductPagingDto);

        return resultMap;
    }

    // 관련 상품 4가지를 얻되 구매수가 많은 것부터 얻음
//    public List<ProductResponseDto.MainProductResponseDto> getRelatedProductList(Long id, String smallCatCd) {
//
//        List<Product> relatedProductList = productRepository.getRelatedProductList(id, smallCatCd);
//
//        return getMainProductResponseDto(relatedProductList);
//    }

    public HashMap<String, Object> getAdminProductList(int page) {
        int realPage = page - 1;
        PageRequest pageable = PageRequest.of(realPage, 10, new Sort(Sort.Direction.DESC, "createdDate"));

        Page<Product> productPage = productRepository.findAll(pageable);

        if (productPage.getTotalElements() > 0) {
            return getAdminProductListMap(productPage, pageable);
        }

        return null;
    }

    // 1차 카테고리 코드와 2차 카테고리 코드로 상품 리스트 조회하기
    @Transactional
    public HashMap<String, Object> getProductListByCatCd(int page, String firstCatCd, String secondCatCd) {
        int realPage = page - 1;
        PageRequest pageable = PageRequest.of(realPage, 10, new Sort(Sort.Direction.DESC, "createdDate"));

        Page<Product> productPage = null;

        if (firstCatCd.equals("ALL") && secondCatCd.equals("ALL")) {
            productPage = productRepository.findAll(pageable);
        } else if (!firstCatCd.equals("ALL") && secondCatCd.equals("ALL")) {
            productPage = productRepository.findAllByLargeCatCd(firstCatCd, pageable);
        } else if (!firstCatCd.equals("ALL")) {
            productPage = productRepository.findByLargeCatCdAndSmallCatCdOrderByCreatedDateDesc(firstCatCd, secondCatCd, pageable);
        } else {
            throw new ProductListException("상품 리스트를 가져올 수 없습니다.");
        }

        if (productPage.getTotalElements() > 0) {
            return getAdminProductListMap(productPage, pageable);
        }

        return null;
    }

    // 상품 추가
    public String addProduct(ProductRequestDto productRequestDto) {

        productRepository.save(Product.builder()
                .productNm(productRequestDto.getProductNm())
                .price(productRequestDto.getPrice())
                .titleImg(productRequestDto.getTitleImg())
                .largeCatCd(productRequestDto.getLargeCatCd())
                .smallCatCd(productRequestDto.getSmallCatCd())
                .productStatus(ProductStatus.SALE)
                .productDisPrcList(new ArrayList<>())
                .questions(new ArrayList<>())
                .carts(new ArrayList<>())
                .productImgList(new ArrayList<>())
                .reviews(new ArrayList<>())
                .purchaseCount(0)
                .limitCount(productRequestDto.getTotalCount())
                .totalCount(productRequestDto.getTotalCount())
                .rateAvg(0)
                .build());

        return "상품이 추가되었습니다.";
    }

    // 상품 상세조회
    public ProductResponseDto.AdminProductDetailResponseDto getAdminProductDetails(Long id) {
        Optional<Product> productOpt = productRepository.findById(id);

        if (!productOpt.isPresent())
            throw new NotExistProductException("존재하지 않는 상품입니다.");

        Product product = productOpt.get();

        int disPrice = 0;
        List<LocalDateTime> disDateList = new ArrayList<>();

        // 할인중인 상품이라면 if문 수행
        if (product.getProductDisPrcList().size() > 0) {
            disPrice = getDisPrice(product);
            disDateList = getDisDateList(product);  // 할인 시작날짜, 종료날짜가 들어있는 리스트
        }

        LocalDateTime disStartDate = null;
        LocalDateTime disEndDate = null;
        String startMonthStr = "";
        String startDayStr = "";
        String endMonthStr = "";
        String endDayStr = "";

        if (disDateList.size() > 0) {
            disStartDate = disDateList.get(0);
            disEndDate = disDateList.get(1);

            startMonthStr = disStartDate.getMonthValue() < 10 ? "0" + disStartDate.getMonthValue() : "" + disStartDate.getMonthValue();
            startDayStr = disStartDate.getDayOfMonth() < 10 ? "0" + disStartDate.getDayOfMonth() : "" + disStartDate.getDayOfMonth();
            endMonthStr = disEndDate.getMonthValue() < 10 ? "0" + disEndDate.getMonthValue() : "" + disEndDate.getMonthValue();
            endDayStr = disEndDate.getDayOfMonth() < 10 ? "0" + disEndDate.getDayOfMonth() : "" + disEndDate.getDayOfMonth();
        }

        return ProductResponseDto.AdminProductDetailResponseDto.builder()
                .id(product.getId())
                .productNm(product.getProductNm())
                .price(product.getPrice())
                .disPrice(disPrice)
                .disStartDt(disStartDate == null ? "" : disStartDate.getYear() + "-" + startMonthStr + "-" + startDayStr)
                .disEndDt(disEndDate == null ? "" : disEndDate.getYear() + "-" + endMonthStr + "-" + endDayStr)
                .titleImg(product.getTitleImg())
                .largeCatCd(product.getLargeCatCd())
                .smallCatCd(product.getSmallCatCd())
                .totalCount(product.getTotalCount())
                .build();
    }

    // 상품 정보 수정
    public String updateProduct(Long id, ProductRequestDto.UpdateRequestDto updateRequestDto) {
        Optional<Product> productOpt = productRepository.findById(id);

        if (!productOpt.isPresent())
            throw new NotExistProductException("존재하지 않는 상품입니다.");

        Product product = productOpt.get();

        product.setProductNm(updateRequestDto.getProductNm());
        product.setPrice(updateRequestDto.getPrice());
        product.setLargeCatCd(updateRequestDto.getLargeCatCd());
        product.setSmallCatCd(updateRequestDto.getSmallCatCd());
        product.setTotalCount(updateRequestDto.getTotalCount());

        productRepository.save(product);

        return "상품 정보 수정이 완료되었습니다.";
    }

    private HashMap<String, Object> getResult(Page<Product> products, Pageable pageable) {
        List<ProductResponseDto> productResponseDtoList = getProductResponseDtoList(products);

        PageImpl<ProductResponseDto> productResponseDtoPage = new PageImpl<>(productResponseDtoList, pageable, products.getTotalElements());

        return getResultMap(productResponseDtoPage);
    }

    private List<ProductResponseDto> getProductResponseDtoList(Page<Product> products) {
        List<ProductResponseDto> productResponseDtoList = new ArrayList<>();

        for (Product product : products) {
            int disPrice = 0;
            if (product.getProductDisPrcList().size() > 0) {
                disPrice = getDisPrice(product);
            }
            productResponseDtoList.add(product.toResponseDto(disPrice));
        }

        return productResponseDtoList;
    }

    private Page<Product> getProductsByCategory(String catCd, Pageable pageable) {
        // 전체 상품 조회
        if(isNull(catCd) || catCd.equals("ALL")) {
            return productRepository.findAll(pageable);
        }
        // 카테고리로 상품 조회
        return productRepository.findBySmallCatCd(catCd, pageable);
    }

    private Page<Product> getProductsByCatCdAndSortCd(String catCd, String sortCd, int page) {
        Pageable pageable = getPageable(page, sortCd);

        // 정렬기준으로 전체상품 조회
        if(isNull(catCd) || catCd.equals("ALL")) {
            return productRepository.findAll(pageable);
        }
        // 카테고리, 정렬기준으로 상품 조회
        return productRepository.findAllByLargeCatCd(catCd, pageable);
    }

    private List<ProductResponseDto.MainProductResponseDto> getMainProductResponseDto(List<Product> products) {
        List<ProductResponseDto.MainProductResponseDto> productResponseDtoList = new ArrayList<>();

        for (Product product : products) {
            int disPrice = 0;
            if (product.getProductDisPrcList().size() > 0) {
                disPrice = getDisPrice(product);
            }
            productResponseDtoList.add(product.toMainProductResponseDto(disPrice));
        }

        return productResponseDtoList;
    }

    // adminProductListDto 조회 공통
    private HashMap<String, Object> getAdminProductListMap(Page<Product> productPage, PageRequest pageable) {
        List<ProductResponseDto.AdminProductResponseDto> productResponseDtoList = new ArrayList<>();

        for (Product product : productPage) {
            int disPrice = 0;
            if (product.getProductDisPrcList().size() > 0) {
                disPrice = getDisPrice(product);
            }
            productResponseDtoList.add(product.toAdminProductResponseDto(disPrice));
        }

        PageImpl<ProductResponseDto.AdminProductResponseDto> adminProductResponseDtoPage
                = new PageImpl<>(productResponseDtoList, pageable, productPage.getTotalElements());

        PagingDto adminProductPagingDto = new PagingDto();
        adminProductPagingDto.setPagingInfo(adminProductResponseDtoPage);

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("adminProductList", adminProductResponseDtoPage);
        resultMap.put("adminProductPagingDto", adminProductPagingDto);

        return resultMap;
    }

    private Pageable getPageable(int realPage, String sortCd) {
        Pageable pageable;

        switch (sortCd) {
            case "new":
                pageable = PageRequest.of(realPage, 9, new Sort(Sort.Direction.DESC, "createdDate"));
                break;
            case "past":
                pageable = PageRequest.of(realPage, 9, new Sort(Sort.Direction.ASC, "createdDate"));
                break;
            case "highPrice":
                pageable = PageRequest.of(realPage, 9, new Sort(Sort.Direction.DESC, "price", "createdDate"));
                break;
            case "lowPrice":
                pageable = PageRequest.of(realPage, 9, new Sort(Sort.Direction.ASC, "price", "createdDate"));
                break;
            case "highSell":
                pageable = PageRequest.of(realPage, 9, new Sort(Sort.Direction.DESC, "purchaseCount", "createdDate"));
                break;
            case "lowSell":
                pageable = PageRequest.of(realPage, 9, new Sort(Sort.Direction.ASC, "purchaseCount", "createdDate"));
                break;
            default:
                throw new NoValidProductSortException("유효하지 않은 상품 정렬입니다.");
        }
        return pageable;
    }

    private int getDisPrice(Product product) {
        // 스트림 API를 사용하여 현재 날짜가 할인이 적용되는 날짜 사이에 있는 데이터 중 가장 할인률이 높은 데이터 하나만을 꺼내서 리스트로 저장
        // 현재 할인이 적용된 리스트 하나를 조회
        List<ProductDisPrc> disprcList
                = product.getProductDisPrcList().stream()
                .filter(productDisPrc -> {
                    LocalDateTime now = LocalDateTime.now();

                    return now.isAfter(productDisPrc.getStartDt())
                            && now.isBefore(productDisPrc.getEndDt());
                })
                .sorted().limit(1).collect(Collectors.toList());

        // 현재 할인이 적용된 리스트가 없을 수도 있으므로 if문 처리
        if (disprcList.size() > 0) {
            return disprcList.get(0).getDisPrc();
        }

        return 0;
    }

    private List<LocalDateTime> getDisDateList(Product product) {
        List<ProductDisPrc> disList
                = product.getProductDisPrcList().stream()
                .filter(productDisPrc -> LocalDateTime.now().isAfter(productDisPrc.getStartDt())
                        && LocalDateTime.now().isBefore(productDisPrc.getEndDt()))
                .sorted().limit(1).collect(Collectors.toList());

        List<LocalDateTime> disDateList = new ArrayList<>();

        if (disList.size() > 0) {
            ProductDisPrc productDisPrc = disList.get(0);

            disDateList.add(productDisPrc.getStartDt());
            disDateList.add(productDisPrc.getEndDt());
        }

        return disDateList;
    }

    private HashMap<String, Object> getResultMap(PageImpl<ProductResponseDto> products) {

        PagingDto questionPagingDto = new PagingDto();
        questionPagingDto.setPagingInfo(products);

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("productList", products);
        resultMap.put("productPagingDto", questionPagingDto);

        // PageImpl 객체를 반환
        return resultMap;
    }

    private ProductResponseDto.SaleProductResponseDto mapToDto(Map<String, Object> map) {

        Product saleProduct = (Product) map.get("product");
        Integer disPrice = (Integer) map.get("disPrc");

        return ProductResponseDto.SaleProductResponseDto.builder()
                .productId(saleProduct.getId())
                .productNm(saleProduct.getProductNm())
                .price(saleProduct.getPrice())
                .titleImg(saleProduct.getTitleImg())
                .rateAvg(saleProduct.getRateAvg())
                .disPrice(disPrice)
                .salePrice((int)((((float) 100 - (float) disPrice) / (float)100) * saleProduct.getPrice()))
                .build();
    }

    public String uploadProductImage(MultipartFile file, String dirName) throws IOException {
        // S3와 연결할 client 얻기
        AmazonS3 s3Client = awss3Utils.getS3Client();

        // S3에 저장할 파일 경로 얻기
        String saveFilePath = UploadFileUtils.getSaveFilePath(file, dirName);

        // S3에 파일 저장 후 url 반환
        return awss3Utils.putObjectToS3AndGetUrl(s3Client, saveFilePath, file);
    }
}

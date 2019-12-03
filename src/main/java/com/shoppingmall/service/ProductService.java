package com.shoppingmall.service;

import com.shoppingmall.common.FileUploadProperties;
import com.shoppingmall.common.UploadFileUtils;
import com.shoppingmall.domain.Product;
import com.shoppingmall.domain.ProductDisPrc;
import com.shoppingmall.domain.UploadFile;
import com.shoppingmall.domain.enums.ProductStatus;
import com.shoppingmall.dto.PagingDto;
import com.shoppingmall.dto.ProductRequestDto;
import com.shoppingmall.dto.ProductResponseDto;
import com.shoppingmall.exception.NoValidProductSortException;
import com.shoppingmall.exception.NotExistProductException;
import com.shoppingmall.exception.ProductListException;
import com.shoppingmall.repository.ProductDisPrcRepository;
import com.shoppingmall.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductService {

    private final Path rootLocation;

    @Autowired
    public ProductService(FileUploadProperties prop) {
        this.rootLocation = Paths.get(prop.getProductUploadDir())
                .toAbsolutePath().normalize();
    }

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductDisPrcRepository productDisPrcRepository;

    // 전체 상품 혹은 카테고리로 상품 조회
    public HashMap<String, Object> getProductListByCategory(String catCd, Pageable pageable, int page) {
        int realPage = page - 1;

        pageable = PageRequest.of(realPage, 9, new Sort(Sort.Direction.DESC, "createdDate"));

        Page<Product> productList;

        if (catCd.equals("ALL")) {
            productList = productRepository.findAll(pageable);
        } else {
            productList = productRepository.findBySmallCatCd(catCd, pageable);
        }

        List<ProductResponseDto> productResponseDtoList = new ArrayList<>();

        for (Product product : productList) {
            int disPrice = 0;
            if (product.getProductDisPrcList().size() > 0) {
                disPrice = getDisPrice(product);
            }
            productResponseDtoList.add(product.toResponseDto(disPrice));
        }

        PageImpl<ProductResponseDto> products = new PageImpl<>(productResponseDtoList, pageable, productList.getTotalElements());
        
        return getResultMap(products);
    }

    // 상품 상세
    public ProductResponseDto getProductDetails(Long id) {
        Optional<Product> productDetails = productRepository.findById(id);

        if (productDetails.isPresent()) {
            Product product = productDetails.get();

            int disPrice = 0;
            if (product.getProductDisPrcList().size() > 0) {
                disPrice = getDisPrice(product);
            }

            return product.toResponseDto(disPrice);
        }
        else
            throw new NotExistProductException("존재하지 않는 상품입니다.");
    }

    public HashMap<String, Object> getProductListByKeyword(int page, String largeCatCd, String sortCd) {
        int realPage = page - 1;

        PageImpl<ProductResponseDto> productResponseDtoPage = checkSort(realPage, largeCatCd, sortCd);

        return getResultMap(productResponseDtoPage);
    }

    public List<ProductResponseDto.MainProductResponseDto> getBestProductList() {

        // @EntityGraph paging 처리 방법
        /*Pageable pageable = PageRequest.of(0, 10, new Sort(Sort.Direction.DESC, "purchaseCount"));
        Page<Product> bestProducts = productRepository.findTop10ByOrderByPurchaseCountDesc(pageable);*/

        List<Product> bestProducts = productRepository.findTop10ByOrderByPurchaseCountDesc();
        bestProducts = bestProducts.stream().limit(10).collect(Collectors.toList());

        List<ProductResponseDto.MainProductResponseDto> bestProductResponseList = new ArrayList<>();

        for (Product product : bestProducts) {
            int disPrice = 0;
            if (product.getProductDisPrcList().size() > 0) {
                disPrice = getDisPrice(product);
            }
            bestProductResponseList.add(product.toMainProductResponseDto(disPrice));
        }

        log.info("##### 여기까지!!");

        return bestProductResponseList;
    }

    public List<ProductResponseDto.MainProductResponseDto> getNewProductList() {

        List<Product> newProducts = productRepository.findTop8ByOrderByCreatedDateDesc();

        List<ProductResponseDto.MainProductResponseDto> newProductResponseList = new ArrayList<>();

        for (Product product : newProducts) {
            int disPrice = 0;
            if (product.getProductDisPrcList().size() > 0) {
                disPrice = getDisPrice(product);
            }
            newProductResponseList.add(product.toMainProductResponseDto(disPrice));
        }

        return newProductResponseList;
    }

    public String initReview(Long productId) {

        Optional<Product> productOpt = productRepository.findById(productId);

        if (!productOpt.isPresent())
            throw new NotExistProductException("존재하지 않는 상품입니다.");

        return productOpt.get().getProductNm();
    }

    // 세일 중인 상품 리스트 얻기
    public HashMap<String, Object> getSaleProductList(int page) {
        int realPage = page - 1;
        PageRequest pageable = PageRequest.of(realPage, 9, new Sort(Sort.Direction.DESC, "createdDate"));

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

        log.info("###### 여기");
        Page<Product> productList = productRepository.findSaleProductList(pageable);
        log.info("###### 여기");

        return resultMap;
    }

    // 관련 상품 4가지를 얻되 구매수가 많은 것부터 얻음
    public List<ProductResponseDto.MainProductResponseDto> getRelatedProductList(Long id, String smallCatCd) {

        List<Product> relatedProductList = productRepository.getRelatedProductList(id, smallCatCd);

        List<ProductResponseDto.MainProductResponseDto> relatedProductResponseDtoList = new ArrayList<>();

        for (Product product : relatedProductList) {
            int disPrice = 0;
            if (product.getProductDisPrcList().size() > 0) {
                disPrice = getDisPrice(product);
            }
            relatedProductResponseDtoList.add(product.toMainProductResponseDto(disPrice));
        }

        return relatedProductResponseDtoList;
    }


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

    // 상품 삭제
    /*public String deleteProduct(Long id) {
        Optional<Product> productOpt = productRepository.findById(id);

        if (!productOpt.isPresent())
            throw new NotExistProductException("존재하지 않는 상품입니다.");

        productRepository.delete(productOpt.get());

        return "상품이 삭제되었습니다.";
    }*/

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

    private PageImpl<ProductResponseDto> checkSort(int realPage, String largeCatCd, String sortCd) {
        Pageable pageable;
        Page<Product> productList;

        if (largeCatCd.equals("ALL")) {
            pageable = getPageable(realPage, sortCd);

            productList = productRepository.findAll(pageable);
        } else {
            pageable = getPageable(realPage, sortCd);

            productList = productRepository.findAllByLargeCatCd(largeCatCd, pageable);
        }

        List<ProductResponseDto> productResponseDtoList = new ArrayList<>();

        for (Product product : productList) {
            int disPrice = 0;
            if (product.getProductDisPrcList().size() > 0) {
                disPrice = getDisPrice(product);
            }

            productResponseDtoList.add(product.toResponseDto(disPrice));
        }

        return new PageImpl<>(productResponseDtoList, pageable, productList.getTotalElements());
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

        // 현재 할인이 적용된 리스트가 없을 수도 있으므로 아래와 같이 if문 처리
        if (disprcList.size() > 0)
            return disprcList.get(0).getDisPrc();

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

    @Transactional
    public UploadFile uploadProductImage(MultipartFile file) throws Exception {
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
}

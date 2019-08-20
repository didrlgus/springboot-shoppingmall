package com.shoppingmall.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shoppingmall.domain.enums.ProductStatus;
import com.shoppingmall.dto.ProductResponseDto;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EntityListeners(AuditingEntityListener.class)
public class Product {

    @Id     // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String productNm;

    @Column(length = 10)
    private String largeCatCd;

    @Column(length = 10)
    private String smallCatCd;

    @Column
    private Integer price;

    @Column
    private Integer purchaseCount;

    @Column
    private Integer limitCount;

    @Column
    private Integer totalCount;

    @Column(length = 10)
    private ProductStatus productStatus;

    @Column(length = 100)
    private String titleImg;

    @Column
    @ColumnDefault("0") //default 0
    private Integer rateAvg;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime updatedDate;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductImg> productImgList;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ProductDisPrc> productDisPrcList;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Question> questions;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Cart> carts;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Review> reviews;

    public ProductResponseDto toResponseDto(int disPrice) {

        return ProductResponseDto.builder()
                .id(id)
                .productNm(productNm)
                .largeCatCd(largeCatCd)
                .smallCatCd(smallCatCd)
                .price(price)
                .disPrice(disPrice)
                .purchaseCount(purchaseCount)
                .limitCount(limitCount)
                .totalCount(totalCount)
                .productStatus(productStatus)
                .rateAvg(rateAvg)
                .titleImg(titleImg)
                .questions(questions)
                .build();
    }

    public ProductResponseDto.MainProductResponseDto toMainProductResponseDto(int disPrice) {

        return ProductResponseDto.MainProductResponseDto.builder()
                .id(id)
                .productNm(productNm)
                .titleImg(titleImg)
                .price(price)
                .disPrice(disPrice)
                .salePrice((int)((((float) 100 - (float) disPrice) / (float)100) * price))
                .rateAvg(rateAvg)
                .build();
    }

    public ProductResponseDto.AdminProductResponseDto toAdminProductResponseDto(int disPrice) {

        return ProductResponseDto.AdminProductResponseDto.builder()
                .id(id)
                .productNm(productNm)
                .titleImg(titleImg)
                .price(price)
                .disPrice(disPrice)
                .purchaseCount(purchaseCount)
                .totalCount(totalCount)
                .rateAvg(rateAvg)
                .build();
    }
}

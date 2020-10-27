package com.shoppingmall.domain.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.quartzscheduler.common.BaseTimeEntity;
import com.shoppingmall.domain.enums.ProductStatus;
import com.shoppingmall.domain.productDisPrc.ProductDisPrc;
import com.shoppingmall.dto.ProductResponseDto;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Product extends BaseTimeEntity {

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

    @Column(length = 150)
    private String titleImg;

    @Column
    @ColumnDefault("0") //default 0
    private Integer rateAvg;

    @BatchSize(size = 1000)
    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ProductDisPrc> productDisPrcList;

    public ProductResponseDto.MainProductResponseDto toMainProductResponseDto(int disPrice) {

        return ProductResponseDto.MainProductResponseDto.builder()
                .id(id)
                .productNm(productNm)
                .titleImg(titleImg)
                .price(price)
                .disPrice(disPrice)
                .salePrice((int)((((float) 100 - (float) disPrice) / (float)100) * price))
                .rateAvg(rateAvg)
                .timestamp(Timestamp.valueOf(this.getCreatedDate()).getTime())
                .purchaseCnt(purchaseCount)
                .build();
    }
}

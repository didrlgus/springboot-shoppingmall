package com.shoppingmall.domain.redis;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;
import java.beans.ConstructorProperties;
import java.io.Serializable;

@ToString
@Getter
@RedisHash("best10-product")
public class Best10Product implements Serializable {

    @Id
    private Long id;
    private String productNm;
    private String titleImg;
    private Integer price;
    private Integer disPrice;
    private Integer salePrice;
    private Integer rateAvg;

    @Builder
    @ConstructorProperties({"id", "productNm", "titleImg", "price", "disPrice", "salePrice", "rateAvg"})
    public Best10Product(Long id, String productNm, String titleImg,
                         Integer price, Integer disPrice, Integer salePrice, Integer rateAvg) {
        this.id = id;
        this.productNm = productNm;
        this.titleImg = titleImg;
        this.price = price;
        this.disPrice = disPrice;
        this.salePrice = salePrice;
        this.rateAvg = rateAvg;
    }

}

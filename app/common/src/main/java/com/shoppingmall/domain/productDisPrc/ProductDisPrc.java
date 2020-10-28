package com.shoppingmall.domain.productDisPrc;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shoppingmall.common.BaseTimeEntity;
import com.shoppingmall.domain.product.Product;
import com.shoppingmall.dto.ProductDisPrcResponseDto;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ProductDisPrc extends BaseTimeEntity implements Comparable<ProductDisPrc> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDateTime startDt;

    @Column
    private LocalDateTime endDt;

    @Column
    private Integer disPrc;

    @Column
    private Character rateYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @JsonIgnore
    private Product product;

    // 가격으로 내림차순 하기 위한 compareTo 재정의
    @Override
    public int compareTo(ProductDisPrc o) {
        return Integer.compare(o.disPrc, this.disPrc);
    }

    public ProductDisPrcResponseDto toResponseDto() {

        String startMonthStr = startDt.getMonthValue() < 10 ? "0" + startDt.getMonthValue() : "" + startDt.getMonthValue();
        String startDayStr = startDt.getDayOfMonth() < 10 ? "0" + startDt.getDayOfMonth() : "" + startDt.getDayOfMonth();
        String endMonthStr = endDt.getMonthValue() < 10 ? "0" + endDt.getMonthValue() : "" + endDt.getMonthValue();
        String endDayStr = endDt.getDayOfMonth() < 10 ? "0" + endDt.getDayOfMonth() : "" + endDt.getDayOfMonth();

        return ProductDisPrcResponseDto.builder()
                .id(id)
                .productId(product.getId())
                .startDt(startDt.getYear() + "-" + startMonthStr + "-" + startDayStr)
                .endDt(endDt.getYear() + "-" + endMonthStr + "-" + endDayStr)
                .disPrc(disPrc)
                .build();
    }
}

package com.shoppingmall.domain.productDisPrc;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.quartzscheduler.common.BaseTimeEntity;
import com.shoppingmall.domain.product.Product;
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

    @Override
    public int compareTo(ProductDisPrc o) {
        return Integer.compare(o.disPrc, this.disPrc);
    }
}

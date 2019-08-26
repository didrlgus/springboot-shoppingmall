package com.shoppingmall.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shoppingmall.dto.CartRequestDto;
import com.shoppingmall.dto.CartResponseDto;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(value = {AuditingEntityListener.class})
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Integer productCount;
    @Column
    private Character useYn;
    @CreatedDate
    private LocalDateTime createdDate;

    // 객체들 간의 관계
    @ManyToOne
    @JoinColumn(name = "normal_user_id", referencedColumnName = "id")
    private NormalUser normalUser;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "product_order_id", referencedColumnName = "id")
    @JsonIgnore
    private ProductOrder productOrder;

    public CartResponseDto toResponseDto(int disPrice) {

        return CartResponseDto.builder()
                .id(id)
                .normalUser(normalUser)
                .product(product)
                .salePrice((int)((((float) 100 - (float) disPrice) / (float)100) * product.getPrice()))
                .productCount(productCount)
                .build();
    }

    // 배치에서 호출하는 메소드
    public Cart setInvalidity() {
        useYn = 'N';
        return this;
    }
}

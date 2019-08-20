package com.shoppingmall.domain;

import com.shoppingmall.dto.CategoryResponseDto;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EntityListeners(AuditingEntityListener.class)
public class ProductCat {

    @Id     // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10)
    private String catCd;

    @Column(length = 50)
    private String catNm;

    @Column(length = 10)
    private String upprCatCd;

    @Column(length = 11)
    private Integer catLv;

    @Column
    private Character useYn;

    @Column
    private String cnntUrl;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime updatedDate;

    public CategoryResponseDto.SecondCategory toSecondCategoryDto() {

        return CategoryResponseDto.SecondCategory.builder()
                .id(id)
                .catCd(catCd)
                .catNm(catNm)
                .upprCatNm(upprCatCd)
                .useYn(useYn)
                .build();
    }
}

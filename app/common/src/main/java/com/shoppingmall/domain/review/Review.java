package com.shoppingmall.domain.review;

import com.shoppingmall.common.BaseTimeEntity;
import com.shoppingmall.domain.product.Product;
import com.shoppingmall.domain.user.User;
import com.shoppingmall.dto.ReviewResponseDto;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(value = {AuditingEntityListener.class})
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String title;
    @Column(length = 100000000)
    private String content;
    @Column
    private int rate;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    public ReviewResponseDto toResponseDto() {
        LocalDateTime createdDate = this.getCreatedDate();

        return ReviewResponseDto.builder()
                .id(id)
                .userIdentifier(user.toReviewResponseDto())
                .title(title)
                .rate(rate)
                .createdDate(createdDate.getYear() + "-" + createdDate.getMonthValue() + "-"
                        + createdDate.getDayOfMonth() + " " + createdDate.getHour() + ":" + createdDate.getMinute() + ":"
                        + createdDate.getSecond())
                .build();
    }

}

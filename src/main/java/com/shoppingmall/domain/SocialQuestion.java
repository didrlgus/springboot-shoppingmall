package com.shoppingmall.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shoppingmall.domain.enums.SocialType;
import com.shoppingmall.dto.SocialQuestionResponseDto;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
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
public class SocialQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private SocialType socialType;
    private String message;
    private boolean answerState;
    private Integer answerCount;
    @CreatedDate
    private LocalDateTime createdDate;
    @LastModifiedDate
    private LocalDateTime updatedDate;

    // 객체들 간의 관계
    @ManyToOne
    @JoinColumn(name = "social_user_id", referencedColumnName = "id")
    private SocialUser socialUser;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @JsonIgnore
    private Product product;

    public SocialQuestionResponseDto toResponseDto() {

        return SocialQuestionResponseDto.builder()
                .id(id)
                .socialUser(socialUser)
                .product(product)
                .socialType(socialType)
                .message(message)
                .answerCount(answerCount)
                .answerState(answerState)
                .createdDate(createdDate)
                .updatedDate(updatedDate)
                .build();
    }


}

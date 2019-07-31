package com.shoppingmall.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shoppingmall.dto.QuestionResponseDto;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "question")
@EntityListeners(value = {AuditingEntityListener.class})
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    private boolean answerState;
    private Integer answerCount;
    @CreatedDate
    private LocalDateTime createdDate;
    @LastModifiedDate
    private LocalDateTime updatedDate;

    // 객체들 간의 관계
    @ManyToOne
    @JoinColumn(name = "normalUser_id", referencedColumnName = "id")
    private NormalUser normalUser;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @JsonIgnore
    private Product product;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<QuestionAnswer> answers;

    public QuestionResponseDto toResponseDto() {

        return QuestionResponseDto.builder()
                .id(id)
                .normalUser(normalUser)
                .product(product)
                .message(message)
                .answerCount(answerCount)
                .answerState(answerState)
                .createdDate(createdDate.getYear() + "-" + createdDate.getMonthValue() + "-"
                        + createdDate.getDayOfMonth() + " " + createdDate.getHour() + ":" + createdDate.getMinute() + ":"
                        + createdDate.getSecond())
                .updatedDate(updatedDate)
                .build();
    }
}

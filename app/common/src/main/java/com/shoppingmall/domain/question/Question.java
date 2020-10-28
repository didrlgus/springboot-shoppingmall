package com.shoppingmall.domain.question;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shoppingmall.common.BaseTimeEntity;
import com.shoppingmall.domain.product.Product;
import com.shoppingmall.domain.questionAnswer.QuestionAnswer;
import com.shoppingmall.domain.user.User;
import com.shoppingmall.dto.QuestionResponseDto;
import lombok.*;
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
public class Question extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    private boolean answerState;
    private Integer answerCount;

    // 객체들 간의 관계
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @JsonIgnore
    private Product product;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<QuestionAnswer> answers;

    public QuestionResponseDto toResponseDto() {
        LocalDateTime createdDate = this.getCreatedDate();
        LocalDateTime modifiedDate = this.getModifiedDate();

        return QuestionResponseDto.builder()
                .id(id)
                .questionUserResponseDto(user.toQuestionResponseDto(user))
                .message(message)
                .answerCount(answerCount)
                .answerState(answerState)
                .createdDate(createdDate.getYear() + "-" + createdDate.getMonthValue() + "-"
                        + createdDate.getDayOfMonth() + " " + createdDate.getHour() + ":" + createdDate.getMinute() + ":"
                        + createdDate.getSecond())
                .modifiedDate(modifiedDate)
                .build();
    }
}

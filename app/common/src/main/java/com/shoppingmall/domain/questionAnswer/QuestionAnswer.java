package com.shoppingmall.domain.questionAnswer;

import com.shoppingmall.common.BaseTimeEntity;
import com.shoppingmall.domain.question.Question;
import com.shoppingmall.domain.user.User;
import com.shoppingmall.dto.QuestionAnswerResponseDto;
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
public class QuestionAnswer extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;

    // 객체들 간의 관계
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    private Question question;

    public QuestionAnswerResponseDto toDto() {
        LocalDateTime createdDate = this.getCreatedDate();

        return QuestionAnswerResponseDto.builder()
                .id(id)
                .user(user)
                .question(question)
                .message(message)
                .createdDate(createdDate.getYear() + "-" + createdDate.getMonthValue() + "-"
                        + createdDate.getDayOfMonth() + " " + createdDate.getHour() + ":" + createdDate.getMinute() + ":"
                        + createdDate.getSecond())
                .build();
    }
}

package com.shoppingmall.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shoppingmall.dto.NormalUserResponseDto;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class NormalUser {

    @Id     // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String identifier;

    @Column
    private String password;

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private String roadAddr;

    @Column
    private String buildingName;

    @Column
    private String detailAddr;

    @Column
    private String authorities;

    @Column
    private Character deleteYn;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @OneToMany(mappedBy = "normalUser", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Question> questions;

    @OneToMany(mappedBy = "normalUser", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<QuestionAnswer> answers;

    @OneToMany(mappedBy = "normalUser", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Cart> carts;

    public NormalUserResponseDto toResponseDto(NormalUser normalUser) {

        return NormalUserResponseDto.builder()
                .id(normalUser.getId())
                .identifier(normalUser.getIdentifier())
                .name(normalUser.getName())
                .email(normalUser.getEmail())
                .roadAddr(normalUser.getRoadAddr())
                .buildingName(normalUser.getBuildingName())
                .detailAddr(normalUser.getDetailAddr())
                .build();
    }
}

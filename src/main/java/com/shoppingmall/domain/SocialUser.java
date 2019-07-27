package com.shoppingmall.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shoppingmall.domain.enums.SocialType;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table
@ToString
public class SocialUser implements Serializable {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String principal;

    @Column
    private String name;

    @Column
    private String email;

    @Column
    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Column
    private String accessToken;

    @Column
    private String authorities;

    @Column
    private String profileImage;

    @Column
    private String roadAddr;

    @Column
    private String buildingName;

    @Column
    private String detailAddr;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime updatedDate;

    @OneToMany(mappedBy = "socialUser")
    @JsonIgnore
    private List<SocialQuestion> socialQuestions;

    @Builder
    public SocialUser(String name, String email, String principal,
                      SocialType socialType, String accessToken, String authorities, String profileImage,
                      LocalDateTime createdDate, LocalDateTime updatedDate) {
        this.name = name;
        this.email = email;
        this.principal = principal;
        this.socialType = socialType;
        this.accessToken = accessToken;
        this.authorities = authorities;
        this.profileImage = profileImage;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

}

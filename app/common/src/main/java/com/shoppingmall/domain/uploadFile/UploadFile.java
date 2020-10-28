package com.shoppingmall.domain.uploadFile;

import com.shoppingmall.common.BaseTimeEntity;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(value = {AuditingEntityListener.class})
public class UploadFile extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String fileName;
    @Column
    private String saveFileName;
    @Column
    private String filePath;
    @Column
    private String contentType;
    @Column
    private Long size;

}

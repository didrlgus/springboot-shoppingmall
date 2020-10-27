package com.shoppingmall.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.UUID;

@Slf4j
public class UploadFileUtils {

    public static final String PRODUCT_UPLOAD_IMAGE = "product-upload-image";
    public static final String REVIEW_UPLOAD_IMAGE = "review-upload-image";

    // 유일한 파일명 생성
    public static String getSaveFilePath(MultipartFile file, String productUploadImage) {
        String random = UUID.randomUUID().toString();
        random = random.replace("-", "");

        String fileExtension = getExtension(Objects.requireNonNull(file.getOriginalFilename()));

        return productUploadImage + "/" + random + "." + fileExtension;
    }

    // 파일의 확장자 반환
    private static String getExtension(String fileName) {
        int dotPosition = fileName.lastIndexOf('.');

        if (-1 != dotPosition && fileName.length() - 1 > dotPosition) {
            return fileName.substring(dotPosition + 1);
        } else {
            return "";
        }
    }
}

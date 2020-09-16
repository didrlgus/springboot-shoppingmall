package com.shoppingmall.common;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@RequiredArgsConstructor
@Component
public class AWSS3Utils {

    private final AWSS3Properties awss3Properties;

    // S3 setting 후 반환
    public AmazonS3 getS3Client() {
        String accessKey = this.awss3Properties.getAccessKey();
        String secretKey = this.awss3Properties.getSecretKey();
        String region = this.awss3Properties.getRegion();

        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

        // s3Client 생성
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region)
                .build();
    }

    // S3에 이미지 파일 저장 후 url 반환
    public String putObjectToS3AndGetUrl(AmazonS3 s3Client, String saveFilePath, MultipartFile file) throws IOException {
        String bucket = this.awss3Properties.getBucket();

        ObjectMetadata objectMetadata = new ObjectMetadata();
        ByteArrayInputStream byteArrayInputStream = getByteArrayInputStream(file, objectMetadata);

        // 이미지 파일 저장
        s3Client.putObject(new PutObjectRequest(bucket, saveFilePath, byteArrayInputStream, objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        return s3Client.getUrl(bucket, saveFilePath).toString();
    }

    private ByteArrayInputStream getByteArrayInputStream(MultipartFile file, ObjectMetadata objectMetadata) throws IOException {
        byte[] bytes = IOUtils.toByteArray(file.getInputStream());
        objectMetadata.setContentLength(bytes.length);

        return new ByteArrayInputStream(bytes);
    }
}

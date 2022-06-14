package com.strebentechnik.logistix.document;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

abstract public class CommonResource {

    @ConfigProperty(name = "bucket.name")
    String bucketName;

    protected PutObjectRequest buildPutRequest(DocumentDto documentDto) {
        return PutObjectRequest.builder()
                .bucket(bucketName)
                .key(documentDto.fileName)
                .contentType(documentDto.mimeType)
                .build();
    }

    protected GetObjectRequest buildGetRequest(String objectKey) {
        return GetObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();
    }

}

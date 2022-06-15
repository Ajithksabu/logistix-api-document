package com.strebentechnik.logistix.document;

import java.io.File;
import java.io.FileInputStream;

import javax.inject.Inject;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.SetBucketPolicyArgs;
import io.quarkus.qute.Engine;
import io.quarkus.qute.Template;

public class DocumentService implements IsDocumentService {
    @Inject
    Engine templateEngine;

    // @ConfigProperty(name = "catalogue-item-bucket")
    // String catalogueItemBucket;

    // @ConfigProperty(name = "use-ssl")
    // String useSsl;

    // @ConfigProperty(name = "host")
    // String host;

    // @ConfigProperty(name = "port")
    // String port;

    // @ConfigProperty(name = "access-key")
    // String accessKey;

    // @ConfigProperty(name = "secret-key")
    // String secretKey;

    private MinioClient minioClient;
    private Template policyTemplate;

    void initializeMinIOClient() throws Exception {
        // Create instance of minio client with configured service details
        minioClient = MinioClient.builder()
                .endpoint("localhost", 9090, false)
                .credentials("minioadmin", "minioadmin")
                .build();

        // Check and create if bucket is available to store catalogue images
        createBucketIfNotExists();
    }

    private void createBucketIfNotExists() throws Exception {
        // Check if the bucket already exists.
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket("catalogue-item-images").build());
        if (found) {
            System.out.println("my-bucketname exists");
        } else {
            System.out.println("my-bucketname does not exist");

            // Prepare Anonymous readonly Policy to fetch objects from bucket without signed
            // Url
            policyTemplate = templateEngine.getTemplate("policy-bucket-catalogueItemImage.json");
            String policy = policyTemplate
                    .data("catalogueItemBucket", "catalogue-item-images")
                    .render();
            minioClient.makeBucket(
                    MakeBucketArgs.builder()
                            .bucket("catalogue-item-images")
                            .build());
            minioClient.setBucketPolicy(
                    SetBucketPolicyArgs.builder().bucket("catalogue-item-images").config(policy).build());
        }
    }

    /**
     * Method to upload the image to minio object store
     * 
     * @param skuNumber
     * @param contentType
     * @param image
     */
    public void uploadCatalogueImage(String skuNumber, String contentType, File image) throws Exception {

        if (minioClient == null)
            initializeMinIOClient();

        minioClient.putObject(
                PutObjectArgs.builder().bucket("catalogue-item-images").object("my-objectname").stream(
                        new FileInputStream(image), image.length(), -1)
                        .contentType(contentType)
                        .build());
    }
}

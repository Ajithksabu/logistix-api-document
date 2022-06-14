package com.strebentechnik.logistix.document;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.StreamingOutput;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@ApplicationScoped
public class AmazonS3 {

	@Inject
	S3Client s3;

	@ConfigProperty(name = "bucket.name")
	String bucketName;

	public PutObjectResponse uploadToS3(DocumentDto documentDto) {
		return s3.putObject(buildPutRequest(documentDto), RequestBody.fromFile(uploadToTemp(documentDto.file)));
	}

	private PutObjectRequest buildPutRequest(DocumentDto documentDto) {
		return PutObjectRequest.builder().bucket(bucketName).key(documentDto.path)
				.contentType(documentDto.mimeType).build();
	}

	private File uploadToTemp(InputStream data) {
		File tempPath;
		try {
			tempPath = File.createTempFile("uploadS3Tmp", ".tmp");
			Files.copy(data, tempPath.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}

		return tempPath;
	}

	public ResponseBuilder downloadFromS3(DocumentDto documentDto) {
		// setS3Credentials();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		GetObjectResponse object = s3.getObject(buildGetRequest(documentDto), ResponseTransformer.toOutputStream(baos));
		ResponseBuilder response = Response.ok((StreamingOutput) output -> baos.writeTo(output));
		response.header("Content-Disposition", "attachment;filename=" + documentDto.fileName);
		response.header("Content-Type", object.contentType());
		return response;
	}

	public File getS3File(DocumentDto documentDto) {
		Path path = Paths.get(documentDto.fileName);
		File existingFile = path.toFile();
		if (existingFile.exists()) {
			existingFile.delete();
		}
		s3.getObject(GetObjectRequest.builder().bucket(bucketName).key(documentDto.path).build(), path);
		File file = path.toFile();
		return file;
	}

	public void deleteS3File(DocumentDto documentDto) {
		s3.deleteObject(DeleteObjectRequest.builder().bucket(bucketName).key(documentDto.path).build());
	}

	private GetObjectRequest buildGetRequest(DocumentDto documentDto) {
		return GetObjectRequest.builder().bucket(bucketName).key(documentDto.path).build();
	}

}

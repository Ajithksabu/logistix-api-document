package com.strebentechnik.logistix.document;

import java.io.File;
import java.time.LocalDateTime;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response.ResponseBuilder;

import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@ApplicationScoped
public class DocumentService {

	@Inject
	DocumentMapper documentMapper;

	@Inject
	AmazonS3 amazonS3;

	@Transactional
	public PutObjectResponse createDocument(DocumentDto documentDto) {

		documentDto.path = getFilePath(documentDto);
		Document document = documentMapper.toEntity(documentDto);
		document.createdAt = LocalDateTime.now();
		document.persist();
		documentDto.id = document.id;
		return amazonS3.uploadToS3(documentDto);
	}

	private String getFilePath(DocumentDto documentDto) {
		return documentDto.profileType + "/" + documentDto.fileName;
	}

	public ResponseBuilder findDocumentById(Long id) {
		Document documentEntity = Document.findById(id);
		DocumentDto documentDto = documentMapper.toResource(documentEntity);
		return amazonS3.downloadFromS3(documentDto);
	}

	public File getFileById(Long id) {
		Document documentEntity = Document.findById(id);
		DocumentDto documentDto = documentMapper.toResource(documentEntity);
		return amazonS3.getS3File(documentDto);
	}

	@Transactional
	public void deleteDocument(Long id) {
		Document documentEntity = Document.findById(id);
		DocumentDto documentDto = documentMapper.toResource(documentEntity);
		amazonS3.deleteS3File(documentDto);
		Document.deleteById(id);
	}

}

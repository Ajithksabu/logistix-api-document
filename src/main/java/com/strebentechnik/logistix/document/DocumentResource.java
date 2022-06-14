package com.strebentechnik.logistix.document;

import java.net.URI;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@Path("/documents")
@Tag(name = "Documents")
public class DocumentResource {

	@Inject
	DocumentService documentService;

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response createDocument(@MultipartForm DocumentDto documentDto) throws Exception {
		PutObjectResponse putResponse = documentService.createDocument(documentDto);

		if (putResponse != null) {
			return Response.created(URI.create(String.valueOf(documentDto.id))).build();
		} else {
			return Response.serverError().build();
		}
	}

}

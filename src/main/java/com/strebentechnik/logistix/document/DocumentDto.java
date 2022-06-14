package com.strebentechnik.logistix.document;

import java.io.InputStream;

import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.providers.multipart.PartType;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class DocumentDto {

	public Long id;

	@FormParam("file")
	@PartType(MediaType.APPLICATION_OCTET_STREAM)
	public InputStream file;

	@FormParam("profileType")
	public String profileType;

	@FormParam("fileName")
	public String fileName;

	@FormParam("mimeType")
	public String mimeType;

	@FormParam("remarks")
	public String remarks;

	public String path;

	@FormParam("size")
	public Long size;

}
package com.strebentechnik.logistix.document;

import java.util.List;

import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface DocumentMapper {

	DocumentDto toResource(Document document);

	List<DocumentDto> map(List<Document> documentEntityList);

	Document toEntity(DocumentDto documentDto);
}

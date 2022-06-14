package com.strebentechnik.logistix.document;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity(name = "documents")
public class Document extends PanacheEntityBase {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;

	public String profileType;

	public Long size;

	public String fileName;

	public String mimeType;

	public String remarks;

	public String path;

	public LocalDateTime createdAt;

	public Document() {

	}

}
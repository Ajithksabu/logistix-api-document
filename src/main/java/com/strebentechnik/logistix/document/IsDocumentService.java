package com.strebentechnik.logistix.document;

import java.io.File;

import javax.enterprise.context.ApplicationScoped;

/**
 * Interface for handling storage requirements
 */
@ApplicationScoped
public interface IsDocumentService {

    /**
     * Upload catalogue image to storage service
     * 
     * @param skuNumber
     * @param contentType
     * @param image
     */
    public void uploadCatalogueImage(String skuNumber, String contentType, File image) throws Exception;
}
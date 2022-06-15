package com.strebentechnik.logistix.document;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.tika.Tika;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

@Path("documents")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class DocumentResource {

    @Inject
    DocumentService documentService;

    @Inject
    DocumentOperator documentOperator;

    @POST
    public Response uploadImage(@PathParam(value = "sku") String skuNumber, InputStream inputStream) throws Exception {
        // Validate skuNumber by Getting catalogue item by sku. If not available,
        // resource not found will be thrown.
        documentOperator.getCatalogueItem(skuNumber);

        // Create temp file from the uploaded image input stream
        File tempFile = createTempFile(skuNumber, inputStream);

        // Validate if the uploaded file is of type image. Else throw error
        Tika tika = new Tika();
        String mimeType = tika.detect(tempFile);

        // Upload the file to storage service
        documentService.uploadCatalogueImage(skuNumber, mimeType, tempFile);

        return Response.status(Response.Status.CREATED).build();
    }

    private File createTempFile(String skuNumber, InputStream inputStream) throws Exception {

        File tempFile = File.createTempFile(skuNumber, ".tmp");
        tempFile.deleteOnExit();

        FileOutputStream out = new FileOutputStream(tempFile);
        IOUtils.copy(inputStream, out);

        return tempFile;

    }

}

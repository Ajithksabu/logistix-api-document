package com.strebentechnik.logistix.document;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DocumentOperator {

    public CatalogueItem getCatalogueItem(String skuNumber) throws Exception {
        return getCatalogueItemBySku(skuNumber);
    }

    private CatalogueItem getCatalogueItemBySku(String skuNumber) throws Exception {
        CatalogueItem catalogueItem = CatalogueItem.find("skuNumber", skuNumber).firstResult();
        return catalogueItem;
    }

}

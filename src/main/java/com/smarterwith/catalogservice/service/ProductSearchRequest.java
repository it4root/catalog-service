package com.smarterwith.catalogservice.service;

import com.smarterwith.catalogservice.CatalogProperties;
import com.smarterwith.catalogservice.entity.ProductSort;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Builder
@Data
public class ProductSearchRequest {
    private final String filter;
    private final String freeTextSearch;
    private final int limit;
    private final int currentPage;
    private final ProductSort sort;

    public ProductSearchRequest(String filter, String freeTextSearch, int limit, int currentPage, ProductSort sort) {
        this.filter = filter;
        this.freeTextSearch = freeTextSearch;
        this.limit = limit;
        this.currentPage = currentPage;
        this.sort = sort;
    }

    public static class ProductSearchRequestBuilder{

        public ProductSearchRequestBuilder sort(String sorting, CatalogProperties catalogProperties ) {
            this.sort = StringUtils.isBlank(sorting) ? ProductSort.getProductSort(catalogProperties.getPagination().getDefaultSort()) : ProductSort.valueOf(sorting);
            return this;
        }


        public ProductSearchRequestBuilder limit(Integer limit, CatalogProperties catalogProperties ) {
            this.limit = (limit == null) ? catalogProperties.getPagination().getDefaultSize() : limit;
            return this;
        }

        public ProductSearchRequestBuilder currentPage(Integer currentPage, CatalogProperties catalogProperties ) {
            this.currentPage =(currentPage == null) ? catalogProperties.getPagination().getDefaultStart() : currentPage;
            return this;
        }
    }

}

package com.smarterwith.catalogservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Product Sorting not found")
public class ProductSortingIllegalArgumentException extends RuntimeException {

    public ProductSortingIllegalArgumentException(String sortingName) {
        super(String.format("Product Sorting not found for sortingName %s", sortingName));
    }
}

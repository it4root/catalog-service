package com.smarterwith.catalogservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(code = HttpStatus.EXPECTATION_FAILED, reason = "Product not saved")
public class ProductNotSavedException extends RuntimeException {

    public ProductNotSavedException(String categoryCode, String articleNumber) {
            super(String.format("Product not saved for categoryCode %s and articleNumber %s", categoryCode, articleNumber));
    }
}

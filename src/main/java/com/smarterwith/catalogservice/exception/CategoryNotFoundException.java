package com.smarterwith.catalogservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Category not found")
public class CategoryNotFoundException extends  RuntimeException{

    public CategoryNotFoundException(String categoryCode) {
        super(String.format("Category not found for category code %s", categoryCode));
    }
}

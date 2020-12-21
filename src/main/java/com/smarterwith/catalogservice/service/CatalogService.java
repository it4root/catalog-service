package com.smarterwith.catalogservice.service;

import com.smarterwith.catalogservice.dto.ProductSearchDto;
import org.springframework.stereotype.Service;

@Service
public class CatalogService {

    public ProductSearchDto searchProduct() {
        return new ProductSearchDto();
    }
}

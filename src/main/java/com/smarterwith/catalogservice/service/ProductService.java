package com.smarterwith.catalogservice.service;

import com.smarterwith.catalogservice.dto.ProductSearchDto;
import com.smarterwith.catalogservice.repository.CategoryRepository;
import com.smarterwith.catalogservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private CategoryRepository categoryRepository;

    private ProductRepository productRepository;


    public ProductSearchDto searchProduct() {
        return new ProductSearchDto();
    }
}

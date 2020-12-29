package com.smarterwith.catalogservice.service;

import com.smarterwith.catalogservice.CatalogProperties;
import com.smarterwith.catalogservice.dto.PaginationDto;
import com.smarterwith.catalogservice.dto.ProductDto;
import com.smarterwith.catalogservice.dto.ProductSearchDto;
import com.smarterwith.catalogservice.entity.Category;
import com.smarterwith.catalogservice.entity.Product;
import com.smarterwith.catalogservice.exception.CategoryNotFoundException;
import com.smarterwith.catalogservice.mapper.ProductMapper;
import com.smarterwith.catalogservice.repository.CategoryRepository;
import com.smarterwith.catalogservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.testcontainers.shaded.org.apache.commons.lang.StringUtils;

import java.util.List;

import static java.util.stream.Collectors.toList;


@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;


    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, CatalogProperties catalogProperties, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
    }

    public ProductSearchDto searchProduct(ProductSearch productSearch) {
        String freeTextSearchDefault = StringUtils.EMPTY;

        Page<Product> productPageResult = productRepository.findAll(PageRequest.of(productSearch.getCurrentPage(), productSearch.getLimit(), productSearch.getSort().getSorting()));

        PaginationDto pagination = PaginationDto.builder()
                .totalSize(Math.toIntExact(productPageResult.getTotalElements()))
                .limit(productPageResult.getSize())
                .currentPage(productSearch.getCurrentPage())
                .build();

        List<ProductDto> products = productPageResult.stream().map(productMapper::toDto).collect(toList());

        return ProductSearchDto.builder()
                .pagination(pagination)
                .products(products)
                .sort(productSearch.getSort().getName())
                .freeTextSearch(freeTextSearchDefault)
                .build();
    }


    public void createProduct(ProductDto productDto) {
        Category category = categoryRepository.findCategoryByCode(productDto.getCategoryCode())
                .orElseThrow(() -> new CategoryNotFoundException(productDto.getCategoryCode()));

        category.addProduct(productMapper.createInitialDomainObject(productDto));
        // TODO: 12/28/2020 conflict 
        categoryRepository.save(category);
    }


}

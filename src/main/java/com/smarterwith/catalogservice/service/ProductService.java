package com.smarterwith.catalogservice.service;

import com.smarterwith.catalogservice.CatalogProperties;
import com.smarterwith.catalogservice.dto.PaginationDto;
import com.smarterwith.catalogservice.dto.ProductDto;
import com.smarterwith.catalogservice.dto.ProductSearchDto;
import com.smarterwith.catalogservice.entity.Category;
import com.smarterwith.catalogservice.entity.Product;
import com.smarterwith.catalogservice.exception.CategoryNotFoundException;
import com.smarterwith.catalogservice.exception.ProductNotSavedException;
import com.smarterwith.catalogservice.mapper.ProductMapper;
import com.smarterwith.catalogservice.repository.CategoryRepository;
import com.smarterwith.catalogservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;

import static java.util.stream.Collectors.toList;


@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final CatalogProperties catalogProperties;


    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, ProductMapper productMapper, CatalogProperties catalogProperties) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
        this.catalogProperties = catalogProperties;
    }


    public ProductSearchRequest buildProductSearchRequest(Integer currentPage, Integer limit, String sorting){
        return  ProductSearchRequest.builder()
                .currentPage(currentPage, catalogProperties)
                .limit(limit, catalogProperties)
                .sort(sorting, catalogProperties)
                .build();
    }


    public ProductSearchDto searchProduct(@NotNull ProductSearchRequest productSearchRequest) {
        Page<Product> productPageResult = productRepository.findAll(PageRequest.of(productSearchRequest.getCurrentPage(), productSearchRequest.getLimit(), productSearchRequest.getSort().getSorting()));

        PaginationDto pagination = PaginationDto.builder()
                .totalSize(Math.toIntExact(productPageResult.getTotalElements()))
                .limit(productPageResult.getSize())
                .currentPage(productSearchRequest.getCurrentPage())
                .build();

        List<ProductDto> products = productPageResult.stream().map(productMapper::toDto).collect(toList());

        return ProductSearchDto.builder()
                .pagination(pagination)
                .products(products)
                .sort(productSearchRequest.getSort().getName())
                .freeTextSearch(productSearchRequest.getFreeTextSearch())
                .build();
    }


    public ProductDto createProduct(@NotNull ProductDto productDto) {
        // TODO: conflict - add the same
        Category category = categoryRepository.findCategoryByCode(productDto.getCategoryCode())
                .orElseThrow(() -> new CategoryNotFoundException(productDto.getCategoryCode()));

        category.addProduct(productMapper.createInitialDomainObject(productDto));
        categoryRepository.saveAndFlush(category);

        return productRepository.findProductByArticleNumber(productDto.getCode()).map(productMapper::toDto)
                .orElseThrow(() -> new ProductNotSavedException(productDto.getCategoryCode(), productDto.getCode()));
    }

}

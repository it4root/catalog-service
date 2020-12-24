package com.smarterwith.catalogservice.service;

import com.google.common.collect.Lists;
import com.smarterwith.catalogservice.dto.PaginationDto;
import com.smarterwith.catalogservice.dto.PriceDto;
import com.smarterwith.catalogservice.dto.ProductDto;
import com.smarterwith.catalogservice.dto.ProductSearchDto;
import com.smarterwith.catalogservice.entity.Category;
import com.smarterwith.catalogservice.entity.Price;
import com.smarterwith.catalogservice.entity.Product;
import com.smarterwith.catalogservice.entity.PublishStatus;
import com.smarterwith.catalogservice.repository.CategoryRepository;
import com.smarterwith.catalogservice.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.testcontainers.shaded.org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;


@Service
public class ProductService {

    private ProductRepository productRepository;

    private CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public ProductSearchDto searchProduct() {
        int startDefault = 0;//TODO move to properties
        int sizeDefault  = 20;
        String sortDefault = "price-desc";
        String freeTextSearchDefault = StringUtils.EMPTY;

        Page<Product> productPageResult = productRepository.findAll(PageRequest.of(startDefault, sizeDefault, Sort.by("price.baseAmount").ascending()));

        PaginationDto pagination = PaginationDto.builder()
                .totalSize(Math.toIntExact(productPageResult.getTotalElements()))
                .limit(productPageResult.getSize())
                .currentPage(startDefault)
                .build();

        List<ProductDto> products = productPageResult.stream().map(p->toDto(p)).collect(toList());

        return ProductSearchDto.builder()
                .pagination(pagination)
                .products(products)
                .sort(sortDefault)
                .freeTextSearch(freeTextSearchDefault)
                .build();
    }

    private Product toDomainObject(ProductDto productDto){
        Product product = new Product();
        product.setName(productDto.getName());
        product.setArticleNumber(productDto.getCode());
        product.setCreatedDate(new Date());
        product.setDescription(productDto.getDescription());
        product.setPublishStatus(PublishStatus.PENDING);
        product.setPrice(toDomainObject(productDto.getPrice()));
        return product;
    }

    private ProductDto toDto(Product product){
        ProductDto dto = new ProductDto();
        dto.setCategoryCode(product.getCategories().get(0).getCode());
        dto.setCode(product.getArticleNumber());
        dto.setDescription(product.getDescription());
        dto.setPrice(toDto(product.getPrice()));
        dto.setName(product.getName());
        return dto;
    }

    private PriceDto toDto(Price price){
        PriceDto priceDto = new PriceDto();
        priceDto.setAmount(price.getBaseAmount().doubleValue());
        priceDto.setCurrencyISO(price.getCurrencyIso());
        return priceDto;
    }

    private Price toDomainObject(PriceDto priceDto){
        Price price = new Price();
        price.setBaseAmount(BigDecimal.valueOf(priceDto.getAmount()));
        price.setCurrencyIso(priceDto.getCurrencyISO());
        return price;
    }

    public void createProduct(ProductDto productDto) {
        Category category = categoryRepository.findCategoryByCode(productDto.getCategoryCode())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found for code "+ productDto.getCategoryCode()));

        category.addProduct(toDomainObject(productDto));

        categoryRepository.save(category);
    }

}

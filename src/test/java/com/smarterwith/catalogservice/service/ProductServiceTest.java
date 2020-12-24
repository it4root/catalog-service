package com.smarterwith.catalogservice.service;

import com.google.common.collect.Lists;
import com.smarterwith.catalogservice.dto.ImageDto;
import com.smarterwith.catalogservice.dto.PriceDto;
import com.smarterwith.catalogservice.dto.ProductDto;
import com.smarterwith.catalogservice.entity.Category;
import com.smarterwith.catalogservice.repository.CategoryRepository;
import com.smarterwith.catalogservice.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

/*
    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void createProduct(){
        ProductDto passedProduct = ProductDto.builder()
                .code("book_one_code")
                .description("book_one_desc")
                .name("book_one_name")
                .price(PriceDto.builder().amount(9.99).currencyISO("USD").build())
                .images(Lists.newArrayList(ImageDto.builder().altText("alt_text").url("image_url").mainImage(false).build()))
                .build();

        Category category = new Category();
        category.setCode("book_category_code");

        given(categoryRepository.findById(any())).willReturn(Optional.of(category));

        ProductDto actualResult = productService.createProduct(passedProduct);

        actualResult.getCode()

    }
*/


}
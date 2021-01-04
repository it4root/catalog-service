package com.smarterwith.catalogservice.service;

import com.smarterwith.catalogservice.dto.PriceDto;
import com.smarterwith.catalogservice.dto.ProductDto;
import com.smarterwith.catalogservice.entity.Category;
import com.smarterwith.catalogservice.entity.Price;
import com.smarterwith.catalogservice.entity.Product;
import com.smarterwith.catalogservice.entity.PublishStatus;
import com.smarterwith.catalogservice.mapper.ProductMapper;
import com.smarterwith.catalogservice.repository.CategoryRepository;
import com.smarterwith.catalogservice.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;


    @Test
    void createProduct() {

        ProductDto productDto = ProductDto.builder()
                .code("book_1_code")
                .name("book_1_name")
                .categoryCode("book_test_category")
                .price(PriceDto.builder().amount(9.99).currencyISO("USD").build())
                .build();


        Product productForTest = ProductForTestBuilder.builder()
                .product("book_1_name", "book_1_code")
                .inCategory("book_test_category")
                .build();


        given(categoryRepository.findCategoryByCode(any())).willReturn(Optional.of(new Category()));
        given(productMapper.createInitialDomainObject(any())).willReturn(productForTest);
        given(productRepository.findProductByArticleNumber(productDto.getCode())).willReturn(Optional.of(productForTest));
        given(productMapper.toDto(productForTest)).willReturn(productDto);

        productService.createProduct(productDto);

        verify(categoryRepository).saveAndFlush(any());
        verifyNoMoreInteractions(categoryRepository);
    }


    static class ProductForTestBuilder {

        private Category category;
        private String productName;
        private String articleNumber;

        public ProductForTestBuilder inCategory(String categoryName) {
            this.category = new Category();
            this.category.setCode(categoryName);
            return this;
        }

        public ProductForTestBuilder product(String productName, String articleNumber) {
            this.productName = productName;
            this.articleNumber = articleNumber;
            return this;
        }

        public static ProductForTestBuilder builder() {
            return new ProductForTestBuilder();
        }

        public Product build() {
            Product product = new Product();
            product.getCategories().add(category);
            product.setName(productName);
            product.setArticleNumber(articleNumber);
            product.setCreatedDate(new Date());
            product.setPublishStatus(PublishStatus.PENDING);

            Price price = new Price();
            price.setBaseAmount(BigDecimal.valueOf(9));
            price.setCurrencyIso("USD");
            product.setPrice(price);

            return product;
        }

    }


}
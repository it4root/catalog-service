package com.smarterwith.catalogservice.integration;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.google.common.collect.Lists;
import com.smarterwith.catalogservice.dto.*;
import com.smarterwith.catalogservice.entity.Category;
import com.smarterwith.catalogservice.entity.Price;
import com.smarterwith.catalogservice.entity.Product;
import com.smarterwith.catalogservice.entity.PublishStatus;
import com.smarterwith.catalogservice.repository.CategoryRepository;
import com.smarterwith.catalogservice.repository.ProductRepository;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;


/*@ActiveProfiles("integration-test")*/

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
//@Transactional(propagation = Propagation.REQUIRED)

@Testcontainers
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=validate"
})

@TestExecutionListeners({
        TransactionDbUnitTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class
})

@SpringBootTest(webEnvironment = RANDOM_PORT)
@DatabaseSetup(value = "/integration/catalog_integration_test_init_data.xml", type = DatabaseOperation.CLEAN_INSERT)
public class CatalogIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;


    @LocalServerPort
    int randomServerPort;

    //TODO: move dockerImageName to properties
    private String dockerImageName = "postgres:12.1";
    @Container
    private PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer(dockerImageName)
            .withDatabaseName("test_catalog_db")
            .withUsername("test_u")
            .withPassword("test_p");

    @Test
    void createProduct_SavedWithResponseCreated() {

        ProductDto expectedResult = ProductDto.builder()
                .code("book_one_code")
                .description("book_one_desc")
                .name("book_one_name")
                .categoryCode("book_test_category")
                //.status() //TODO
                .price(PriceDto.builder().amount(9.99).currencyISO("USD").build())
                .images(Lists.newArrayList(ImageDto.builder().altText("alt_text").url("image_url").mainImage(false).build()))
                .build();

        HttpEntity<ProductDto> request = new HttpEntity<>(expectedResult);

        ResponseEntity<ProductDto> actualResponse = restTemplate.postForEntity("/v1/api/catalogs/products", request, ProductDto.class);

        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    }



    @Test
    void createProduct_MultipleSaveProductToTheSameCategory() {

        ProductDto expectedResult1 = ProductDto.builder()
                .code("book_1_code")
                .description("book_1_desc")
                .name("book_1_name")
                .categoryCode("book_test_category")
                .price(PriceDto.builder().amount(9.99).currencyISO("USD").build())
                .build();

       ProductDto expectedResult2 = ProductDto.builder()
                .code("book_2_code")
                .description("book_2_desc")
                .name("book_2_name")
                .categoryCode("book_test_category")
                .price(PriceDto.builder().amount(29.99).currencyISO("USD").build())
                .build();

        ProductDto expectedResult3 = ProductDto.builder()
                .code("book_3_code")
                .description("book_3_desc")
                .name("book_3_name")
                .categoryCode("book_test_category")
                .price(PriceDto.builder().amount(19.99).currencyISO("USD").build())
                .build();


        ResponseEntity<ProductDto> actualResponse1 = restTemplate.postForEntity("/v1/api/catalogs/products", new HttpEntity<>(expectedResult1), ProductDto.class);
        ResponseEntity<ProductDto> actualResponse2 = restTemplate.postForEntity("/v1/api/catalogs/products", new HttpEntity<>(expectedResult2), ProductDto.class);
        ResponseEntity<ProductDto> actualResponse3 = restTemplate.postForEntity("/v1/api/catalogs/products", new HttpEntity<>(expectedResult3), ProductDto.class);

        assertThat(actualResponse1.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(actualResponse2.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(actualResponse3.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ResponseEntity<ProductSearchDto> actualResponse = restTemplate.getForEntity("/v1/api/catalogs/products/search", ProductSearchDto.class);

        assertTrue(actualResponse.getStatusCode().is2xxSuccessful());
        assertThat(actualResponse.getBody().getPagination().getTotalSize()).isEqualTo(3);
        assertThat(actualResponse.getBody().getPagination().getCurrentPage()).isEqualTo(0);
        assertThat(actualResponse.getBody().getPagination().getLimit()).isEqualTo(20);

        List<Double> actualPriceSort = actualResponse.getBody().getProducts().stream().map(p -> p.getPrice().getAmount()).collect(Collectors.toList());
        assertThat(actualPriceSort)
                .isNotEmpty()
                .hasSize(3)
                .doesNotHaveDuplicates()
                .containsExactly(9.99,19.99, 29.99);
    }


    @Test
    void getProductSearchNoParams_ReturnProductListWithDefaultSettings() {


        ProductDto expectedProduct = ProductDto.builder()
                .code("book_two_code")
                .description("book_two_desc")
                .name("book_two_name")
                .categoryCode("book_test_category")
                //.status() //TODO
                .price(PriceDto.builder().amount(9.99).currencyISO("USD").build())
                //TODO .images(Lists.newArrayList(ImageDto.builder().altText("alt_text").url("image_url").mainImage(false).build()))
                .build();

        HttpEntity<ProductDto> request = new HttpEntity<>(expectedProduct);

        ResponseEntity response = restTemplate
                .exchange("/v1/api/catalogs/products", HttpMethod.POST, request, ProductDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ProductSearchDto expectedResult = ProductSearchDto.builder()
                .freeTextSearch(StringUtils.EMPTY)
                .sort("price-desc")
                .pagination(PaginationDto.builder().currentPage(0).limit(20).totalSize(1).build())
                .products(Lists.newArrayList(
                        expectedProduct
                )).build();

        ResponseEntity<ProductSearchDto> actualResponse = restTemplate.getForEntity("/v1/api/catalogs/products/search", ProductSearchDto.class);

        assertTrue(actualResponse.getStatusCode().is2xxSuccessful());

        assertThat(expectedResult).isEqualToComparingOnlyGivenFields(actualResponse.getBody());
    }

}

package com.smarterwith.catalogservice.integration;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.google.common.collect.Lists;
import com.smarterwith.catalogservice.dto.*;
import com.smarterwith.catalogservice.entity.Category;
import com.smarterwith.catalogservice.entity.Product;
import com.smarterwith.catalogservice.repository.CategoryRepository;
import com.smarterwith.catalogservice.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;


/*@ActiveProfiles("integration-test")*/

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional(propagation = Propagation.NOT_SUPPORTED)

@Testcontainers
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=validate"
})

@TestExecutionListeners({
        TransactionalTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class
})

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class CatalogIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @LocalServerPort
    int randomServerPort;

    @Container
    private PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer()
            .withDatabaseName("test_catalog_db")
            .withUsername("test_u")
            .withPassword("test_p");

    @Test
    @DatabaseSetup(value = "/integration/catalog_integration_test_init_data.xml")
    void categoryGRUD_CheckRepository() {
        final Optional<Category> actualResult = categoryRepository.findAll().stream().filter(f -> "book_test_category" .equals(f.getCode())).findAny();
        assertThat(actualResult).isPresent();
    }

    @Test
    void productGRUD_CheckRRepository() {
        List<Product> actualResult = productRepository.findAll();
        assertThat(actualResult).hasSize(0);
    }

    @Test
    void getProductSearchNoParams_ReturnProductListWithDefaultSettings() {

        ProductSearchDto expectedResult = ProductSearchDto.builder()
                .freeTextSearch(StringUtils.EMPTY)
                .categoryCode("book")
                .sort("price-desc")
                .pagination(PaginationDto.builder().currentPage(1).limit(20).totalSize(1).build())
                .products(Lists.newArrayList(
                        ProductDto.builder()
                                .code("book_one_code")
                                .description("book_one_desc")
                                .name("book_one_name")
                                .price(PriceDto.builder().amount(9.99).currencyISO("USD").build())
                                .images(Lists.newArrayList(ImageDto.builder().altText("alt_text").url("image_url").mainImage(false).build()))
                                .build()
                )).build();

        ResponseEntity<ProductSearchDto> actualResponse = restTemplate.getForEntity("/v1/api/products/search", ProductSearchDto.class);

        assertTrue(actualResponse.getStatusCode().is2xxSuccessful());

        assertThat(expectedResult).isEqualTo(actualResponse);
    }

}

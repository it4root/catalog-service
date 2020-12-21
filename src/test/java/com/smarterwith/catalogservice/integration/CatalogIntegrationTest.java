package com.smarterwith.catalogservice.integration;

import com.google.common.collect.Lists;
import com.smarterwith.catalogservice.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.apache.commons.lang.StringUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;


@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class CatalogIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    int randomServerPort;

    @Container
    private PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer()
            .withDatabaseName("foo")
            .withUsername("foo")
            .withPassword("secret");


    @Test
    void getProductSearchNoParams_ReturnProductListWIthDefaultSettings() {

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

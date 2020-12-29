package com.smarterwith.catalogservice.integration;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.google.common.collect.Lists;
import com.smarterwith.catalogservice.dto.PaginationDto;
import com.smarterwith.catalogservice.dto.PriceDto;
import com.smarterwith.catalogservice.dto.ProductDto;
import com.smarterwith.catalogservice.dto.ProductSearchDto;
import com.smarterwith.catalogservice.entity.PublishStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;


@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Testcontainers
@TestExecutionListeners({
        TransactionDbUnitTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class
})

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class CatalogIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Container
    private static final PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer("postgres:12.1")
            .withDatabaseName("test_catalog_db")
            .withUsername("test_u")
            .withPassword("test_p");


    @DatabaseSetup(value = "/integration/catalog_integration_test_init_data.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "/integration/catalog_integration_test_clean_data.xml", type = DatabaseOperation.DELETE_ALL)
    @Test
    void createProduct_MultipleSaveProductToTheSameCategory() {
        //Given: products prepared
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

        //When: post products
        ResponseEntity<ProductDto> actualResponse1 = restTemplate.postForEntity("/v1/api/catalogs/products", new HttpEntity<>(expectedResult1), ProductDto.class);
        ResponseEntity<ProductDto> actualResponse2 = restTemplate.postForEntity("/v1/api/catalogs/products", new HttpEntity<>(expectedResult2), ProductDto.class);
        ResponseEntity<ProductDto> actualResponse3 = restTemplate.postForEntity("/v1/api/catalogs/products", new HttpEntity<>(expectedResult3), ProductDto.class);
        //And: get statuses created
        assertThat(actualResponse1.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(actualResponse2.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(actualResponse3.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        //And: do search with default params
        ResponseEntity<ProductSearchDto> actualResponse = restTemplate.getForEntity("/v1/api/catalogs/products/search", ProductSearchDto.class);

        //Than: check search returns successfully
        assertTrue(actualResponse.getStatusCode().is2xxSuccessful());
        assertThat(actualResponse.getBody()).isNotNull();
        //And: returns correct pagination settings
        assertThat(actualResponse.getBody().getPagination().getTotalSize()).isEqualTo(3);
        assertThat(actualResponse.getBody().getPagination().getCurrentPage()).isEqualTo(0);
        assertThat(actualResponse.getBody().getPagination().getLimit()).isEqualTo(20);
        //And: returns correct sorting desc by price
        List<Double> actualPriceSort = actualResponse.getBody().getProducts().stream().map(p -> p.getPrice().getAmount()).collect(Collectors.toList());
        assertThat(actualPriceSort)
                .isNotEmpty()
                .hasSize(3)
                .doesNotHaveDuplicates()
                .containsExactly(9.99, 19.99, 29.99);
    }

    @DatabaseSetup(value = "/integration/catalog_integration_test_init_data.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "/integration/catalog_integration_test_clean_data.xml", type = DatabaseOperation.DELETE_ALL)
    @Test
    void getProductSearchNoParams_ReturnProductListWithDefaultSettings() {
        //Given: products prepared
        ProductDto expectedProduct = ProductDto.builder()
                .code("book_two_code")
                .description("book_two_desc")
                .name("book_two_name")
                .categoryCode("book_test_category")
                .status(PublishStatus.PENDING.name())
                .price(PriceDto.builder().amount(9.99).currencyISO("USD").build())
                .build();

        //When: post product
        HttpEntity<ProductDto> request = new HttpEntity<>(expectedProduct);
        ResponseEntity<ProductDto> response = restTemplate
                .exchange("/v1/api/catalogs/products", HttpMethod.POST, request, ProductDto.class);

        //Than: check status is created
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        //And: expected search dto structure equals actual search dto
        ResponseEntity<ProductSearchDto> actualSearchResponse = restTemplate.getForEntity("/v1/api/catalogs/products/search", ProductSearchDto.class);
        ProductSearchDto expectedSearchResult = ProductSearchDto.builder()
                .freeTextSearch(StringUtils.EMPTY)
                .sort("price-desc")
                .pagination(PaginationDto.builder().currentPage(0).limit(20).totalSize(1).build())
                .products(Lists.newArrayList(
                        expectedProduct
                )).build();
        assertTrue(actualSearchResponse.getStatusCode().is2xxSuccessful());
        assertThat(expectedSearchResult).isEqualToComparingOnlyGivenFields(actualSearchResponse.getBody());
    }

    //TODO check .images(Lists.newArrayList(ImageDto.builder().altText("alt_text").url("image_url").mainImage(false).build()))

    //TODO modify product-change fields but no status
    //TODO check modify status from pending to active -> should trigger rabbit and push notification to subscribed workers and send update to stock service
    //TODO delete product and check only with active and pending statuses return



}

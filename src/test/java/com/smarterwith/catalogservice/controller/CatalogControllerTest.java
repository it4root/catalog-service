package com.smarterwith.catalogservice.controller;

import com.google.common.collect.Lists;
import com.smarterwith.catalogservice.CatalogProperties;
import com.smarterwith.catalogservice.dto.PaginationDto;
import com.smarterwith.catalogservice.dto.PriceDto;
import com.smarterwith.catalogservice.dto.ProductDto;
import com.smarterwith.catalogservice.dto.ProductSearchDto;
import com.smarterwith.catalogservice.entity.PublishStatus;
import com.smarterwith.catalogservice.repository.CategoryRepository;
import com.smarterwith.catalogservice.repository.ProductRepository;
import com.smarterwith.catalogservice.service.ProductSearchRequest;
import com.smarterwith.catalogservice.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.testcontainers.shaded.org.apache.commons.lang.StringUtils;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class CatalogControllerTest {

    @MockBean
    private ProductService productService;

    @MockBean
    private CatalogProperties catalogProperties;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private CategoryRepository categoryRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getProductSearch_WithNoRequestParams_ReturnJsonProductsWithDefaultPagination() throws Exception {

        ProductSearchDto productSearchResult = ProductSearchDto.builder()
                .freeTextSearch(StringUtils.EMPTY)
                .sort("price-desc")
                .pagination(PaginationDto.builder().currentPage(0).limit(20).totalSize(1).build())
                .products(Lists.newArrayList(
                        ProductDto.builder()
                                .code("book_one_code")
                                .categoryCode("cat_code")
                                .description("book_one_desc")
                                .status(PublishStatus.PENDING.name())
                                .name("book_one_name")
                                .price(PriceDto.builder().amount(9.99).currencyISO("USD").build())
                                .build()
                )).build();


        given(productService.buildProductSearchRequest(anyInt(), anyInt(), anyString())).willReturn(mock(ProductSearchRequest.class));
        given(productService.searchProduct(any())).willReturn(productSearchResult);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get("/v1/api/catalogs/products/search");
        mockMvc.perform(mockHttpServletRequestBuilder.contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.freeTextSearch", blankString()))
                .andExpect(jsonPath("$.sort", is("price-desc")))

                .andExpect(jsonPath("$.pagination.currentPage", is(0)))
                .andExpect(jsonPath("$.pagination.limit", is(20)))
                .andExpect(jsonPath("$.pagination.totalSize", is(1)))

                .andExpect(jsonPath("$.products[0].code", is("book_one_code")))
                .andExpect(jsonPath("$.products[0].name", is("book_one_name")))
                .andExpect(jsonPath("$.products[0].description", is("book_one_desc")))
                .andExpect(jsonPath("$.products[0].categoryCode", is("cat_code")))
                .andExpect(jsonPath("$.products[0].status", is("PENDING")))

                .andExpect(jsonPath("$.products[0].price.amount", is(9.99)))
                .andExpect(jsonPath("$.products[0].price.currencyISO", is("USD")));

    }


}


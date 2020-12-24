package com.smarterwith.catalogservice.controller;

import com.google.common.collect.Lists;
import com.smarterwith.catalogservice.dto.*;
import com.smarterwith.catalogservice.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.org.apache.commons.lang.StringUtils;

import static org.hamcrest.Matchers.blankString;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class CatalogControllerTest {

    @MockBean
    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getProductSearch_HasCorrectJsonStructure() throws Exception {
        ProductSearchDto productSearchResult = ProductSearchDto.builder()
                .freeTextSearch(StringUtils.EMPTY)
              //TODOD  .categoryCode("book")
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

        given(productService.searchProduct()).willReturn(productSearchResult);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get("/v1/api/products/search");

        mockMvc.perform(mockHttpServletRequestBuilder.contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.freeTextSearch", blankString()))
                .andExpect(jsonPath("$.sort", is("price-desc")))
                .andExpect(jsonPath("$.categoryCode", is("book")))

                .andExpect(jsonPath("$.pagination.currentPage", is(1)))
                .andExpect(jsonPath("$.pagination.limit", is(20)))
                .andExpect(jsonPath("$.pagination.totalSize", is(1)))

                .andExpect(jsonPath("$.products[0].code", is("book_one_code")))
                .andExpect(jsonPath("$.products[0].name", is("book_one_name")))
                .andExpect(jsonPath("$.products[0].description", is("book_one_desc")))
                .andExpect(jsonPath("$.products[0].images[0].url", is("image_url")))
                .andExpect(jsonPath("$.products[0].images[0].altText", is("alt_text")))
                .andExpect(jsonPath("$.products[0].images[0].mainImage", is(false)))
                .andExpect(jsonPath("$.products[0].price.amount", is(9.99)))
                .andExpect(jsonPath("$.products[0].price.currencyISO", is("USD")));
    }


    //@RepeatedTest(3)
    //void postCatalogShouldReturnIdempotentResponse() {
    //  System.out.println("Executing repeated test");

    //}
}
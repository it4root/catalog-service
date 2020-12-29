package com.smarterwith.catalogservice.controller;

import com.smarterwith.catalogservice.CatalogProperties;
import com.smarterwith.catalogservice.dto.ProductDto;
import com.smarterwith.catalogservice.dto.ProductSearchDto;
import com.smarterwith.catalogservice.entity.ProductSort;
import com.smarterwith.catalogservice.service.ProductSearch;
import com.smarterwith.catalogservice.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@AllArgsConstructor
@Api("/v1/api/catalogs")
@RequestMapping("/v1/api/catalogs")
@RestController
public class CatalogController {

    private ProductService productService;

    private CatalogProperties catalogProperties;

    @GetMapping("/health")
    public String health() {
        return "OK";
    }

    @GetMapping("/products/search")
    @ApiOperation(value = "Returns a list of products and additional data such as available sorting, paging  and other options. It can include recommendation suggestions.")
    public ProductSearchDto getProductSearch(@ApiParam(value = "The format of a serialized filter", example = "filter=key1:value1:key2:value2") @RequestParam(required = false, defaultValue = StringUtils.EMPTY) String filter,
                                             @ApiParam(value = "Free text search", example = "any words") @RequestParam(required = false, defaultValue = StringUtils.EMPTY) String freeTextSearch,
                                             @ApiParam(value = "Number of products returned", defaultValue = "20") @RequestParam(required = false) Integer limit,
                                             @ApiParam(value = "The current page requested", defaultValue = "1") @RequestParam(required = false) Integer currentPage,
                                             @ApiParam(value = "The current page sorting", defaultValue = "price-desc") @RequestParam(required = false) String sorting) {


        ProductSearch productSearch = ProductSearch.builder()
                .filter(filter)
                .currentPage(currentPage, catalogProperties)
                .freeTextSearch(freeTextSearch)
                .limit(limit, catalogProperties)
                .sort(sorting, catalogProperties)
                .build();

        return productService.searchProduct(productSearch);
    }

    @PostMapping("/products")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create product")
    public void createProduct(@Valid @RequestBody ProductDto productDto) {
        productService.createProduct(productDto);
    }

    //TODO: /products/recommendations
    //TODO: /products/{productCode}/reviews

}
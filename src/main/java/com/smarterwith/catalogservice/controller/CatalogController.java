package com.smarterwith.catalogservice.controller;

import com.smarterwith.catalogservice.CatalogProperties;
import com.smarterwith.catalogservice.dto.ProductDto;
import com.smarterwith.catalogservice.dto.ProductSearchDto;
import com.smarterwith.catalogservice.service.ProductSearchRequest;
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


    @GetMapping("/health")
    public String health() {
        return "OK";
    }


    @GetMapping("/products/search")
    @ApiOperation(value = "Returns a list of products and additional data such as sorting and pagination settings")
    public ProductSearchDto getProductSearch(@ApiParam(value = "Number of products returned") @RequestParam(required = false) Integer limit,
                                             @ApiParam(value = "The current page requested") @RequestParam(required = false) Integer currentPage,
                                             @ApiParam(value = "The current page sorting") @RequestParam(required = false) String sorting) {



        return productService.searchProduct(productService.buildProductSearchRequest(limit, currentPage, sorting));
    }

    @PostMapping("/products")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create product")
    public void createProduct(@Valid @RequestBody ProductDto productDto) {
        productService.createProduct(productDto);
    }

}
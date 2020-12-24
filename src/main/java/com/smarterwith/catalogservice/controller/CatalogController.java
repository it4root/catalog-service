package com.smarterwith.catalogservice.controller;

import com.smarterwith.catalogservice.dto.ProductDto;
import com.smarterwith.catalogservice.dto.ProductSearchDto;
import com.smarterwith.catalogservice.service.ProductService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
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
    public ProductSearchDto getProductSearch() {
        return productService.searchProduct();
    }

    @PostMapping("/products")
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@Valid @RequestBody ProductDto productDto){
        productService.createProduct(productDto);
    }

    //TODO: /products/recommendations
    //TODO: /products/{productCode}/reviews

}
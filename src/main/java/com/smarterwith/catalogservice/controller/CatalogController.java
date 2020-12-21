package com.smarterwith.catalogservice.controller;

import com.smarterwith.catalogservice.dto.ProductSearchDto;
import com.smarterwith.catalogservice.service.CatalogService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;



@AllArgsConstructor
@Api("/v1/api/catalogs")
@RequestMapping("/v1/api")
@RestController
public class CatalogController {

    private CatalogService catalogService;

    @GetMapping("/catalogs/health")
    public String health() {
        return "OK";
    }

    @GetMapping("/products/search")
    public ProductSearchDto getProductSearch() {
        return catalogService.searchProduct();
    }
}
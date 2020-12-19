package com.smarterwith.catalogservice.controller;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@Api("/v1/api/catalogs")
@RequestMapping("/v1/api")
public class CatalogController {

    @GetMapping("/catalogs/health")
    public String health() {
        return "OK";
    }
}
package com.smarterwith.catalogservice.dto;


import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonRootName("productSearch")
public class ProductSearchDto {

    private String freeTextSearch;
    private String sort;
    private String categoryCode;
    private List<ProductDto> products;
    private PaginationDto pagination;

}

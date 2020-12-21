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
@JsonRootName("product")
public class ProductDto {

    private String name;
    private String description;
    private String code;
    private List<ImageDto> images;
    private PriceDto price;

}

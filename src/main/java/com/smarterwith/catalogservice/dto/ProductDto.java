package com.smarterwith.catalogservice.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;

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
    private String categoryCode;
    private PriceDto price;

}

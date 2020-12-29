package com.smarterwith.catalogservice.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.smarterwith.catalogservice.entity.Product;
import com.smarterwith.catalogservice.entity.PublishStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonRootName("product")
public class ProductDto {

    @ApiModelProperty(required = true, value = "Product name")
    @NotBlank
    private String name;

    @ApiModelProperty(required = true, value = "Product description")
    @NotBlank
    private String description;

    @ApiModelProperty(required = true, value = "Product article number")
    @NotBlank
    private String code;
    @Null
    private List<@Valid ImageDto> images;

    @ApiModelProperty(required = true, value = "Products in categories")
    @NotBlank
    private String categoryCode;

    @ApiModelProperty(value = "Publishing status")
    private String status;

    @NotNull
    private @Valid PriceDto price;

}

package com.smarterwith.catalogservice.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@ApiModel(value = "Product price")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonRootName("price")
public class PriceDto {

    @ApiModelProperty(required = true, value = "Product base price")
    @PositiveOrZero
    private Double amount;

    @ApiModelProperty(required = true, value = "Currency ISO")
    @NotBlank
    private String currencyISO;
}

package com.smarterwith.catalogservice.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@ApiModel(value = "Contains the settings for result page for product search")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonRootName("pagination")
public class PaginationDto {

    @ApiModelProperty(required = true, value = "The current page number. The first page is zero")
    @PositiveOrZero
    private int currentPage;

    @ApiModelProperty(required = true, value = "The max value for products per page. Max value is 50")
    @Positive
    @Max(value = 50)
    private int limit;

    @ApiModelProperty(required = true, value = "The total number of matched products")
    @PositiveOrZero
    private int totalSize;

}

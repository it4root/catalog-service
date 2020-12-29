package com.smarterwith.catalogservice.dto;


import com.fasterxml.jackson.annotation.JsonRootName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel(value = "Containing the result page for product search")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonRootName("productSearch")
public class ProductSearchDto {
    @ApiModelProperty(required = true, value = "Free text for search")
    @NotNull
    private String freeTextSearch;
    @ApiModelProperty(required = true, value = "Sorting type")
    @NotBlank
    private String sort;
    private List<@Valid ProductDto> products;
    private @Valid PaginationDto pagination;

}

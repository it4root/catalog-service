package com.smarterwith.catalogservice.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@ApiModel(value = "Contains the images for product")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonRootName("image")
public class ImageDto {

    @ApiModelProperty(required = true, value = "The url to file storage")
    @NotBlank
    private String url;

    @ApiModelProperty(required = true, value = "The text for alt tag")
    @NotBlank
    private String altText;

    @ApiModelProperty(required = true, value = "Main image flag. If no image with this flag true - first uploaded will be define as main")
    @NotEmpty
    private boolean mainImage;
}

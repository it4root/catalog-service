package com.smarterwith.catalogservice.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonRootName("image")
public class ImageDto {
    private String url;
    private String altText;
    private boolean mainImage;
}

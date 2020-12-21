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
@JsonRootName("price")
public class PriceDto {
    private Double amount;
    private String currencyISO;

}

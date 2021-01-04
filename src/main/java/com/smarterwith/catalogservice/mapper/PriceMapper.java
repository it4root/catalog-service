package com.smarterwith.catalogservice.mapper;

import com.smarterwith.catalogservice.dto.PriceDto;
import com.smarterwith.catalogservice.entity.Price;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@AllArgsConstructor
public class PriceMapper {

    public PriceDto toDto(Price price){
        PriceDto priceDto = new PriceDto();
        priceDto.setAmount(price.getBaseAmount().doubleValue());
        priceDto.setCurrencyISO(price.getCurrencyIso());
        return priceDto;
    }

    public Price toDomainObject(PriceDto priceDto){
        Price price = new Price();
        price.setBaseAmount(BigDecimal.valueOf(priceDto.getAmount()));
        price.setCurrencyIso(priceDto.getCurrencyISO());
        return price;
    }
}

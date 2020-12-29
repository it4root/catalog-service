package com.smarterwith.catalogservice.mapper;

import com.smarterwith.catalogservice.dto.ProductDto;
import com.smarterwith.catalogservice.entity.Product;
import com.smarterwith.catalogservice.entity.PublishStatus;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ProductMapper {

    @Autowired
    private PriceMapper priceMapper;

    public Product toDomainObject(ProductDto productDto){
        Product product = new Product();
        product.setName(productDto.getName());
        product.setArticleNumber(productDto.getCode());
        product.setDescription(productDto.getDescription());
        product.setPrice(priceMapper.toDomainObject(productDto.getPrice()));
        return product;
    }

    public Product createInitialDomainObject(ProductDto productDto){
        Product product = new Product();
        product.setName(productDto.getName());
        product.setArticleNumber(productDto.getCode());
        product.setCreatedDate(new Date());
        product.setDescription(productDto.getDescription());
        product.setPublishStatus(PublishStatus.PENDING);
        product.setPrice(priceMapper.toDomainObject(productDto.getPrice()));
        return product;
    }

    public ProductDto toDto(Product product){
        ProductDto dto = new ProductDto();
        dto.setCategoryCode(product.getCategories().get(0).getCode());
        dto.setCode(product.getArticleNumber());
        dto.setDescription(product.getDescription());
        dto.setPrice(priceMapper.toDto(product.getPrice()));
        dto.setName(product.getName());
        return dto;
    }
}

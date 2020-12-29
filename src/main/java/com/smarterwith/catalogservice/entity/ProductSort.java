package com.smarterwith.catalogservice.entity;

import com.smarterwith.catalogservice.exception.ProductSortingIllegalArgumentException;
import org.springframework.data.domain.Sort;

import java.util.stream.Stream;

public enum ProductSort {

    PRICE_DESC("price-desc", Sort.by("price.baseAmount").ascending()),
    PRICE_ASC("price-asc", Sort.by("price.baseAmount").descending());

    private final String name;
    private final Sort sorting;

    ProductSort(String name, Sort sorting) {
        this.name = name;
        this.sorting = sorting;
    }

    public static ProductSort getProductSort(String sortingName) {
        return Stream.of(ProductSort.values())
                .filter(p -> p.getName().equals(sortingName))
                .findAny().orElseThrow(() -> new ProductSortingIllegalArgumentException(sortingName));
    }

    public String getName() {
        return name;
    }

    public Sort getSorting() {
        return sorting;
    }
}

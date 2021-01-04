package com.smarterwith.catalogservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Data
@Component
@ConfigurationProperties(
        prefix = "catalog"
)
public class CatalogProperties {

    private String name;
    private Pagination pagination;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Pagination{
        @NotNull
        private int defaultSize;

        @NotNull
        private int defaultStart;

        @NotNull
        private String defaultSort;

    }

}

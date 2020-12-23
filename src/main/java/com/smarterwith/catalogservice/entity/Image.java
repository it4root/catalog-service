package com.smarterwith.catalogservice.entity;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@Entity
@Table(name = "image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "altText", nullable = false)
    private String altText;

    @Column(name = "mainImage")
    private boolean mainImage;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return isMainImage() == image.isMainImage() &&
                getId().equals(image.getId()) &&
                getUrl().equals(image.getUrl()) &&
                Objects.equals(getAltText(), image.getAltText()) &&
                Objects.equals(getProduct(), image.getProduct());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUrl());
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }

    public boolean isMainImage() {
        return mainImage;
    }

    public void setMainImage(boolean mainImage) {
        this.mainImage = mainImage;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}

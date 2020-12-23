package com.smarterwith.catalogservice.entity;

import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue
    private UUID id;

    @Version
    private Long version;

    @NaturalId
    @Column(name = "articleNumber", nullable = false)
    private String articleNumber;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_date")
    private Date modifiedDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date")
    private Date createdDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "price_id")
    private Price price;

    @Column(name = "publish_status_id", nullable = false, unique = false)
    @Enumerated(EnumType.STRING)
    private PublishStatus publishStatus;

    @OneToMany(
            mappedBy = "product",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true
    )
    private List<Image> images;

    @ManyToMany(mappedBy = "products")
    private List <Category> categories;


    @Override
    public int hashCode() {
        return Objects.hash(getArticleNumber());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return getId().equals(product.getId()) &&
                getVersion().equals(product.getVersion()) &&
                getArticleNumber().equals(product.getArticleNumber()) &&
                getName().equals(product.getName()) &&
                Objects.equals(getDescription(), product.getDescription()) &&
                Objects.equals(getPrice(), product.getPrice()) &&
                getPublishStatus() == product.getPublishStatus() &&
                Objects.equals(getImages(), product.getImages()) &&
                Objects.equals(getCategories(), product.getCategories());
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getArticleNumber() {
        return articleNumber;
    }

    public void setArticleNumber(String articleNumber) {
        this.articleNumber = articleNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public PublishStatus getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(PublishStatus publishStatus) {
        this.publishStatus = publishStatus;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}

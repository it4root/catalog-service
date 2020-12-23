package com.smarterwith.catalogservice.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


@NoArgsConstructor
@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue
    private UUID id;

    @Version
    private Long version;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "description")
    private String description;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    @ManyToMany
    @JoinTable(
            name = "product_rel_category",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> products;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(getId(), category.getId()) &&
                Objects.equals(getCode(), category.getCode()) &&
                Objects.equals(getDescription(), category.getDescription()) &&
                Objects.equals(getDisplayName(), category.getDisplayName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCode(), getDescription(), getDisplayName());
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}

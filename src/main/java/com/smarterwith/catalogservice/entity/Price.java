package com.smarterwith.catalogservice.entity;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@Entity
@Table(name = "price")
public class Price {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "currency_iso")
    private String currencyIso;

    @Column(name = "base_amount")
    private BigDecimal baseAmount;

    @OneToOne(mappedBy = "price")
    private Product product;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return  getCurrencyIso().equals(price.getCurrencyIso()) &&
                getBaseAmount().equals(price.getBaseAmount()); //&&
                //getProduct().equals(price.getProduct()
               // );
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCurrencyIso(), getBaseAmount(), getProduct());
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCurrencyIso() {
        return currencyIso;
    }

    public void setCurrencyIso(String currencyIso) {
        this.currencyIso = currencyIso;
    }

    public BigDecimal getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(BigDecimal baseAmount) {
        this.baseAmount = baseAmount;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}

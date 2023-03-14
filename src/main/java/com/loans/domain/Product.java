package com.loans.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.StringJoiner;

@Entity
@Table(name = "product")
public class Product extends AbstractEntity implements Serializable {

    @NotNull
    @Column(nullable = false, unique = true)
    private String name;

    @NotNull
    @Column(name = "max_allowed_limit", nullable = false, scale = 2)
    @Min(1)
    private BigDecimal maxAllowedLimit;

    @NotNull
    @Column(name = "interest_percentage", nullable = false, scale = 2)
    @Min(1)
    private BigDecimal interestPercentage;

    @NotNull
    @Column(nullable = false)
    @Min(1)
    private int tenure;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    public Product() {}

    public static Product newInstance(String name, double maxAllowedLimit, float percentage, int tenure) {
        return new Product(name,
                BigDecimal.valueOf(maxAllowedLimit),
                BigDecimal.valueOf(percentage),
                tenure);
    }

    private Product(String name, BigDecimal maxAllowedLimit, BigDecimal interestPercentage, int tenure) {
        this.name = name;
        this.maxAllowedLimit = maxAllowedLimit;
        this.interestPercentage = interestPercentage;
        this.tenure = tenure;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getMaxAllowedLimit() {
        return maxAllowedLimit;
    }

    public void setMaxAllowedLimit(BigDecimal maxAllowedLimit) {
        this.maxAllowedLimit = maxAllowedLimit;
    }

    public BigDecimal getInterestPercentage() {
        return interestPercentage;
    }

    public void setInterestPercentage(BigDecimal interestPercentage) {
        this.interestPercentage = interestPercentage;
    }

    public int getTenure() {
        return tenure;
    }

    public void setTenure(int tenure) {
        this.tenure = tenure;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        if (!super.equals(o)) return false;
        Product product = (Product) o;
        return getName().equals(product.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getName());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Product.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("maxAllowedLimit=" + maxAllowedLimit)
                .add("interestPercentage=" + interestPercentage)
                .add("tenure=" + tenure)
                .add("isActive=" + isActive)
                .add("id=" + getId())
                .toString();
    }
}

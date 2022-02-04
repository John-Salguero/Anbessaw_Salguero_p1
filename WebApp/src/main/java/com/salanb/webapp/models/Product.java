package com.salanb.webapp.models;

import java.math.BigDecimal;

public class Product {

    private Integer id;

    private String name;

    private BigDecimal price;

    private Boolean availability;

    public Product() {

    }

    public Product(Product p) {
        this.id = p.id;
        this.name = p.name;
        this.price = p.price;
        this.availability = p.availability;

    }

    public Product(Integer id, String name, BigDecimal price, Boolean availability) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.availability = availability;
    }

    public Product(String name, BigDecimal price, Boolean availability) {
        this.name = name;
        this.price = price;
        this.availability = availability;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Boolean getAvailability() {
        return availability;
    }

    public void setAvailability(Boolean availability) {
        this.availability = availability;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", availability=" + availability +
                '}';

    }
}
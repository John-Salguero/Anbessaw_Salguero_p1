package com.salanb.orm.models;

import java.math.BigDecimal;
import java.text.NumberFormat;

// Model is deigned to represent Data that we want to maintain within our application
public class Movie {

    private int id;
    private String title;
    private BigDecimal price;
    private boolean available;
    private long returnDate;

    // Default Constructor
    public Movie() {
    }

    // Full argument constructor
    public Movie(int id, String title, BigDecimal price, boolean available, long returnDate) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.available = available;
        this.returnDate = returnDate;
    }

    // ID-less constructor
    public Movie(String title, BigDecimal price, boolean available, long returnDate) {
        this.title = title;
        this.price = price;
        this.available = available;
        this.returnDate = returnDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public long getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(long returnDate) {
        this.returnDate = returnDate;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price=" + NumberFormat.getCurrencyInstance().format(price) +
                ", available=" + available +
                ", returnDate=" + returnDate +
                '}';
    }
}

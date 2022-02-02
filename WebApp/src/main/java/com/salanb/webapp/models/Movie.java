package com.salanb.webapp.models;

import java.math.BigDecimal;

// Model is deigned to represent Data that we want to maintain within our application
public class Movie {

    private int id;
    private String title;
    private BigDecimal price;
    private boolean available;
    private long returnDate;
    private Integer directorId;

    // Default Constructor
    public Movie() {
    }

    // Copy Constructor
    public Movie(Movie m) {
        this.id = m.id;
        this.title = m.title;
        this.price = m.price;
        this.available = m.available;
        this.returnDate = m.returnDate;
        this.returnDate = m.returnDate;
        this.directorId = m.directorId;
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

    public int getDirectorId() {
        return directorId;
    }

    public void setDirectorId(int directorId) {
        this.directorId = directorId;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", available=" + available +
                ", returnDate=" + returnDate +
                ", directorId=" + directorId +
                '}';
    }
}

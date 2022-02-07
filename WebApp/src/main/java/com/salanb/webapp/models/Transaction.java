package com.salanb.webapp.models;


import java.math.BigDecimal;

public class Transaction {

    private Integer id;
    private Integer customerId;
    private Long date;
    private BigDecimal subtotal;

    public Transaction() {
        id = 0;
    }

    public Transaction(int id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
}


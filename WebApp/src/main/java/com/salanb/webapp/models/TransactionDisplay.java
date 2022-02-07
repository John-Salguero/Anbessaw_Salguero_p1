package com.salanb.webapp.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionDisplay {

    private Integer id;
    private Integer customerId;
    private Date date;
    private List<CartItem> toys;

    public TransactionDisplay(Transaction transaction){
        this.id = transaction.getId();
        this.customerId = transaction.getCustomerId();
        this.date = new Date(transaction.getDate());
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<CartItem> getToys() {
        return toys;
    }

    public void setToys(List<CartItem> toys) {
        this.toys = toys;
    }
}

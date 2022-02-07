package com.salanb.webapp.models;

import java.util.List;

public class CartItem {
    private Product item;
    private int amount;

    public CartItem(){};

    public CartItem(Product item, int amount){
        this.item = item;
        this.amount = amount;
    }

    public Product getItem() {
        return item;
    }

    public void setItem(Product item) {
        this.item = item;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}

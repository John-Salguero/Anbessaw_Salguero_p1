package com.salanb.webapp.models;

public class Cart {

    private Integer productId;
    private Integer customerId;
    private Integer amount;

    public Cart(int uid, int id) {
        this.productId = id;
        this.customerId = uid;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }
}


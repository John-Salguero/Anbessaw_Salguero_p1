package com.salanb.webapp.models;

import java.math.BigDecimal;

public class TransactionProduct {

    private Integer amount;
    private Integer transactionId;
    private Integer productId;

    public TransactionProduct(int transactionId, int productId){
        this.transactionId = transactionId;
        this.productId = productId;
    }

    public TransactionProduct() {
        transactionId = 0;
        productId = 0;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }
}

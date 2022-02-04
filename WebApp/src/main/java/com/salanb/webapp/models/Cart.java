package com.salanb.webapp.models;

public class Cart {

    private Integer amount;

    private Integer c_id;

    private Integer p_id;

    public Cart() {

    }

    public Cart(Integer amount, Integer c_id, Integer p_id) {
        this.amount = amount;
        this.c_id = c_id;
        this.p_id = p_id;
    }

    public Cart (Integer amount) {
        this.amount = amount;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getC_id() {
        return c_id;
    }

    public void setC_id(Integer c_id) {
        this.c_id = c_id;
    }

    public Integer getP_id() {
        return p_id;
    }

    public void setP_id(Integer p_id) {
        this.p_id = p_id;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "amount=" + amount +
                ", c_id=" + c_id +
                ", p_id=" + p_id +
                '}';
    }
}


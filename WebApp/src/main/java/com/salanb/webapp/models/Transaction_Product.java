package com.salanb.webapp.models;

public class Transaction_Product {

    private Integer amount;

    private Integer t_id;

    private Integer p_id;

    public Transaction_Product () {

    }

    public Transaction_Product(Integer amount, Integer t_id, Integer p_id) {
        this.amount = amount;
        this.t_id = t_id;
        this.p_id = p_id;
    }
    public Transaction_Product(Integer amount) {
        this.amount = amount;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getT_id() {
        return t_id;
    }

    public void setT_id(Integer t_id) {
        this.t_id = t_id;
    }

    public Integer getP_id() {
        return p_id;
    }

    public void setP_id(Integer p_id) {
        this.p_id = p_id;
    }

    @Override
    public String toString() {
        return "Transaction_Product{" +
                "amount=" + amount +
                ", t_id=" + t_id +
                ", p_id=" + p_id +
                '}';
    }
}

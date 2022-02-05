package com.salanb.webapp.models;


public class Transaction {

    private Integer id;

    private Long date;

    private Integer c_id;

    public Transaction () {

    }

    public Transaction(Integer id, Long date, Integer c_id) {
        this.id = id;
        this.date = date;
        this.c_id = c_id;

    }
    public Transaction(Long date) {
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public int getC_id() {
        return c_id;
    }

    public void setC_id(Integer c_id) {
        this.c_id = c_id;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", date=" + date +
                ", c_id=" + c_id +
                '}';
    }
}


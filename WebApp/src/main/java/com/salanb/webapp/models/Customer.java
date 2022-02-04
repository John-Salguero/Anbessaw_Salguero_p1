package com.salanb.webapp.models;

public class Customer {

    private Integer id;

    private String userName;

    private String passWord;

        public Customer() {
        }

        public Customer(Customer c) {
            this.id = c.id;
            this.userName = c.userName;
            this.passWord = c.passWord;
        }

        public Customer(int id, String userName, String passWord) {
            this.userName = userName;
            this.passWord = passWord;
        }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;


    }
    @Override
    public String toString () {
            return "Customer{" +
                    "id=" + id +
                    ", userName" + userName +
                    ", passWord" + passWord +
                    ')';
    }
}


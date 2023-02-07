package com.my.android_app.entity;

public class User extends Entity{
    private String name;
    private int price;
    private int payedPrice;
    private String color;
    private boolean isDelete;

    public User(int id, String name, int price, int payedPrice, String color) {
        super(id);
        this.name = name;
        this.price = price;
        this.payedPrice = payedPrice;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPayedPrice() {
        return payedPrice;
    }

    public void setPayedPrice(int payedPrice) {
        this.payedPrice = payedPrice;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }
}

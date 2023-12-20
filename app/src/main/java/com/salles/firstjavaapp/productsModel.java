package com.salles.firstjavaapp;

public class productsModel {
    String name, count;
    Object stock;

    public productsModel(String name, String count, Object stock) {
        this.name = name;
        this.count = count;
        this.stock = stock;
    }

    public String getName() {
        return name;
    }

    public String getCount() {
        return count;
    }

    public String getStock() {
        return String.valueOf(stock);
    }
}

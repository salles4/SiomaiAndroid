package com.salles.firstjavaapp;

public class productsModel {
    String name;
    int count, stock;

    public productsModel(String name, int count, int stock) {
        this.name = name;
        this.count = count;
        this.stock = stock;
    }

    public String getName() {
        return name;
    }

    public String getCount() {
        return String.valueOf(count);
    }

    public String getStock() {
        return String.valueOf(stock);
    }
}

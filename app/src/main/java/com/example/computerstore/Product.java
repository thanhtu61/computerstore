package com.example.computerstore;

import android.widget.ImageView;

public class Product {
    private ImageView img;
    private int productId;
    private String productName;
    private String description;
    private double price;
    private double discount;
    private int stockQuantity;
    private int categoryId;

    // Constructor
    public Product(int productId, String productName, String description, double price, double discount, int stockQuantity, int categoryId) {
        this.productId = productId;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.stockQuantity = stockQuantity;
        this.categoryId = categoryId;
    }

    // Getter and Setter methods

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}

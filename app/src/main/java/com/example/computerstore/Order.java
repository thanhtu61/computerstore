package com.example.computerstore;

public class Order {
    private int orderId;
    private int clientId;
    private int productId;
    private String productName;
    private double price;
    private int quantity;
    private double total;
    private String dateAdded;

    // Constructor
    public Order(int orderId, int clientId, int productId, String productName, double price, int quantity, double total, String dateAdded) {
        this.orderId = orderId;
        this.clientId = clientId;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.total = total;
        this.dateAdded = dateAdded;
    }

    // Getter and Setter methods

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int OrderId) {
        this.orderId = OrderId;
    }
    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }
}


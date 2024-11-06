package com.example.computerstore;

import java.util.Date;

public class Cart {
    private int cartId;
    private int clientId;
    private int productId;
    private double price;
    private double discount;
    private double price_d;
    private int quantity;
    private Date dateAdded;
    private double Total;
    private String ProductName;
    private String imgProduct;

    public Cart(int cartId, int clientId, int productId, double price, int quantity, Date dateAdded) {
        this.cartId = cartId;
        this.clientId = clientId;
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
        this.dateAdded = dateAdded;
    }
    public Cart(int cartId, String productName, double price, int quantity, String imgproduct, double discount, int productId) {
        this.cartId = cartId;
       this.ProductName = productName;
        this.price = price;
        this.quantity = quantity;
        this.imgProduct = imgproduct;
        this.discount=discount;
        this.productId= productId;

    }

    // Getters and Setters
    public int getCartId() { return cartId; }
    public int getClientId() { return clientId; }
    public int getProductId() { return productId; }
    public double getPrice() { return price; }
    public double getPrice_d() { return price * (1 - discount / 100)*quantity; }
    public double getTotal() { return price*0.01*discount; }
    public int getQuantity() { return quantity; }
    public Date getDateAdded() { return dateAdded; }
    public String getImgProduct(){return imgProduct;}
    public String getProductName(){return ProductName;}
    public void setQuantity(int quantity){this.quantity=quantity;}
    public void setPrice_d(double Price_d){this.price_d = Price_d;}
}
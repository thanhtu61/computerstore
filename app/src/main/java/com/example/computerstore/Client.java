package com.example.computerstore;

public class Client {
    private int id;
    private String username;
    private String phone;
    private String address;

    public Client(int id, String username, String phone, String address) {
        this.id = id;
        this.username = username;
        this.phone = phone;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
}

package com.example.computerstore;

public class Client {
    private int id;
    private String username;
    private String phone;
    private String email;
    private String address;

    public Client(int id, String username, String email,String phone, String address) {
        this.id = id;
        this.username = username;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }
    public Client( String username, String email) {
        this.username = username;

        this.email = email;

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
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
}

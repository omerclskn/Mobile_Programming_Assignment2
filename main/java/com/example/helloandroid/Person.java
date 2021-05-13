package com.example.helloandroid;

public class Person {
    private int id;
    private String name;
    private String surname;
    private String email;
    private String phone;
    private String password;
    private byte[] image_uri;

    public byte[] getImage_uri() {
        return image_uri;
    }

    public void setImage_uri(byte[] image_uri) {
        this.image_uri = image_uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}

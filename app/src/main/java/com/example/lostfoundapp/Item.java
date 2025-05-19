package com.example.lostfoundapp;

public class Item {
    int id;
    String type;
    String name;
    String phone;
    String description;
    String date;
    String location;

    public Item(int id, String type, String name, String phone, String description, String date, String location) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.phone = phone;
        this.description = description;
        this.date = date;
        this.location = location;
    }

    public Item(String type, String name, String phone, String description, String date, String location) {
        this.type = type;
        this.name = name;
        this.phone = phone;
        this.description = description;
        this.date = date;
        this.location = location;
    }

    public int getId() { return id; }
    public String getType() { return type; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getDescription() { return description; }
    public String getDate() { return date; }
    public String getLocation() { return location; }

    public void setId(int id) { this.id = id; }
    public void setType(String type) { this.type = type; }
    public void setName(String name) { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setDescription(String description) { this.description = description; }
    public void setDate(String date) { this.date = date; }
    public void setLocation(String location) { this.location = location; }
}

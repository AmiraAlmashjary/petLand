package com.company.petLand.models;

public class Account {
    public String id;
    public String name;
    public String email;
    public String description;
    public Long phone;
    public String type;


    public String status = "Available";

    public Account(String id, String name, String email, Long phone, String type, String status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.type = type;
        this.status = status;
    }

    public Account() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public void setType(String type) {
        this.type = type;
    }


}

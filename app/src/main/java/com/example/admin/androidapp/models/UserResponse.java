package com.example.admin.androidapp.models;

/**
 * Created by Admin on 3/4/2017.
 */

public class UserResponse {
    private String objectId;
    private String email;
    private String description;
    private String photo;
    private String name;

    public String getId() {
        return objectId;
    }

    public String getEmail() {
        return email;
    }

    public String getDescription() {
        return description;
    }

    public String getPhoto() {
        return photo;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }
}

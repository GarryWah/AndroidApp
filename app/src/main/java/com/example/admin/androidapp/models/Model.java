package com.example.admin.androidapp.models;

/**
 * Created by Admin on 3/5/2017.
 */
public class Model {
    private String name;
    private String description;
    private String photo;
    private String objectId;

    public String getId() {
        return objectId;
    }

    public void setId(String id) {
        this.objectId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "Model{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }
}

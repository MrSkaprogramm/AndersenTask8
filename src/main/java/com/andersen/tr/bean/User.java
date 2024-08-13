package com.andersen.tr.bean;

import java.time.LocalDateTime;

public class User {
    private int id;
    private String name;
    private LocalDateTime creationDateTime;

    public User(int id, String name, LocalDateTime creationDateTime) {
        this.id = id;
        this.name = name;
        this.creationDateTime = creationDateTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(LocalDateTime creationDateTime) {
        this.creationDateTime = creationDateTime;
    }
}

package com.andersen.tr.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "\"User\"", schema = "public")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDateTime;

    public User() {
    }

    public User(String name, LocalDateTime creationDateTime) {
        this.name = name;
        this.creationDateTime = creationDateTime;
    }

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

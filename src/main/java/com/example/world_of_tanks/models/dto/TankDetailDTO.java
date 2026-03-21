package com.example.world_of_tanks.models.dto;

import com.example.world_of_tanks.models.enums.CategoryEnum;

import java.time.LocalDate;

public class TankDetailDTO {

    private long id;
    private String name;
    private String description;
    private long health;
    private long power;
    private LocalDate created;
    private CategoryEnum categoryName;
    private String categoryDescription;
    private String ownerUsername;

    public TankDetailDTO() {
    }

    public long getId() {
        return id;
    }

    public TankDetailDTO setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public TankDetailDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public TankDetailDTO setDescription(String description) {
        this.description = description;
        return this;
    }

    public long getHealth() {
        return health;
    }

    public TankDetailDTO setHealth(long health) {
        this.health = health;
        return this;
    }

    public long getPower() {
        return power;
    }

    public TankDetailDTO setPower(long power) {
        this.power = power;
        return this;
    }

    public LocalDate getCreated() {
        return created;
    }

    public TankDetailDTO setCreated(LocalDate created) {
        this.created = created;
        return this;
    }

    public CategoryEnum getCategoryName() {
        return categoryName;
    }

    public TankDetailDTO setCategoryName(CategoryEnum categoryName) {
        this.categoryName = categoryName;
        return this;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public TankDetailDTO setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
        return this;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public TankDetailDTO setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
        return this;
    }
}

package com.example.world_of_tanks.models.dto;

import java.time.LocalDate;

public class TankEventDTO {

    private String tankName;
    private String username;
    private String category;
    private long health;
    private long power;
    private LocalDate created;
    private String action;

    public TankEventDTO() {
    }

    public TankEventDTO(String tankName, String username, String category, long health, long power, LocalDate created, String action) {
        this.tankName = tankName;
        this.username = username;
        this.category = category;
        this.health = health;
        this.power = power;
        this.created = created;
        this.action = action;
    }


    public String getTankName() {
        return tankName;
    }

    public void setTankName(String tankName) {
        this.tankName = tankName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getHealth() {
        return health;
    }

    public void setHealth(long health) {
        this.health = health;
    }

    public long getPower() {
        return power;
    }

    public void setPower(long power) {
        this.power = power;
    }

    public LocalDate getCreated() {
        return created;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}

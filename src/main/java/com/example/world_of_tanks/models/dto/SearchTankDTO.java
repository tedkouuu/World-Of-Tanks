package com.example.world_of_tanks.models.dto;

public class SearchTankDTO {

    private String name;
    private String health;
    private String power;

    public SearchTankDTO() {
    }

    public String getName() {
        return name;
    }

    public SearchTankDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getHealth() {
        return health;
    }

    public SearchTankDTO setHealth(String health) {
        this.health = health;
        return this;
    }

    public String getPower() {
        return power;
    }

    public SearchTankDTO setPower(String power) {
        this.power = power;
        return this;
    }

    public boolean isEmpty() {
        return (name == null || name.trim().isEmpty()) &&
                (health == null || health.trim().isEmpty()) &&
                (power == null || power.trim().isEmpty());
    }

    public Integer getHealthAsInteger() {
        try {
            return health != null && !health.trim().isEmpty() ? Integer.parseInt(health.trim()) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Integer getPowerAsInteger() {
        try {
            return power != null && !power.trim().isEmpty() ? Integer.parseInt(power.trim()) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

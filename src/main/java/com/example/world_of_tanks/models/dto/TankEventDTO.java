package com.example.world_of_tanks.models.dto;

public class TankEventDTO {
    private String tankName;
    private String action;

    public TankEventDTO() {
    }

    public TankEventDTO(String tankName, String action) {
        this.tankName = tankName;
        this.action = action;
    }

    public String getTankName() {
        return tankName;
    }

    public void setTankName(String tankName) {
        this.tankName = tankName;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}

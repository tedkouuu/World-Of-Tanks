package com.example.world_of_tanks.models.dto;

import com.example.world_of_tanks.models.validation.TankExist;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class DeleteUserTankDTO {


    @NotBlank(message = "Tank name is required!")
    @Size(min = 2, max = 10, message = "Tank name must be between 2 and 10 characters!")
    @TankExist
    private String name;

    public String getName() {
        return name;
    }

    public DeleteUserTankDTO setName(String name) {
        this.name = name;
        return this;
    }
}

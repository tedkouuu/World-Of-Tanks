package com.example.world_of_tanks.seeders;

import com.example.world_of_tanks.services.UserRoleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(0)
public class UserRoleSeeder implements CommandLineRunner {

    private final UserRoleService userRoleService;

    public UserRoleSeeder(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @Override
    public void run(String... args) throws Exception {

        this.userRoleService.seedUserRoles();

    }
}

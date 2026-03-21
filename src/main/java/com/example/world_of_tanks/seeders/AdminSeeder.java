package com.example.world_of_tanks.seeders;

import com.example.world_of_tanks.models.UserEntity;
import com.example.world_of_tanks.models.UserRoleEntity;
import com.example.world_of_tanks.models.enums.UserRoleEnum;
import com.example.world_of_tanks.repositories.UserRepository;
import com.example.world_of_tanks.repositories.UserRoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(2)
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminSeeder(UserRepository userRepository,
                       UserRoleRepository userRoleRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        if (userRepository.findByUsername("admin").isPresent()) {
            return;
        }

        UserRoleEntity adminRole = userRoleRepository.findByUserRole(UserRoleEnum.ADMIN);
        UserRoleEntity userRole = userRoleRepository.findByUserRole(UserRoleEnum.USER);

        if (adminRole == null || userRole == null) {
            return;
        }

        UserEntity admin = new UserEntity()
                .setUsername("admin")
                .setFullName("System Administrator")
                .setEmail("admin@worldoftanks.com")
                .setPassword(passwordEncoder.encode("admin123"))
                .setRoles(List.of(adminRole, userRole));

        userRepository.save(admin);
    }
}

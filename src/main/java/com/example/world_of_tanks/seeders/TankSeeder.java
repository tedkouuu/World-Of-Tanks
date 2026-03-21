package com.example.world_of_tanks.seeders;

import com.example.world_of_tanks.models.Category;
import com.example.world_of_tanks.models.Tank;
import com.example.world_of_tanks.models.UserEntity;
import com.example.world_of_tanks.models.enums.CategoryEnum;
import com.example.world_of_tanks.repositories.CategoryRepository;
import com.example.world_of_tanks.repositories.TankRepository;
import com.example.world_of_tanks.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@Order(3)
public class TankSeeder implements CommandLineRunner {

    private final TankRepository tankRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public TankSeeder(TankRepository tankRepository,
                      CategoryRepository categoryRepository,
                      UserRepository userRepository) {
        this.tankRepository = tankRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {

        if (tankRepository.count() > 0) {
            return;
        }

        Optional<UserEntity> adminOpt = userRepository.findByUsername("admin");
        if (adminOpt.isEmpty()) {
            return;
        }

        UserEntity admin = adminOpt.get();
        Category light = categoryRepository.findByName(CategoryEnum.LIGHT_TANK);
        Category medium = categoryRepository.findByName(CategoryEnum.MEDIUM_TANK);
        Category heavy = categoryRepository.findByName(CategoryEnum.HEAVY_TANK);

        if (light == null || medium == null || heavy == null) {
            return;
        }

        List<Tank> tanks = List.of(
                tank("T-34", "The legendary Soviet medium tank that became the backbone of the Red Army. Known for its sloped armor and reliability.", 380, 850, medium, admin),
                tank("Tiger I", "Germany's fearsome heavy tank with the powerful 88mm gun. A symbol of armored warfare in World War II.", 450, 1200, heavy, admin),
                tank("M4 Sherman", "America's most produced tank of WWII. Versatile and dependable, it served on every front.", 340, 780, medium, admin),
                tank("Panzer IV", "The workhorse of the German Panzerwaffe. Continuously upgraded throughout the war.", 320, 720, medium, admin),
                tank("IS-2", "Stalin's heavy breakthrough tank with a devastating 122mm gun. Feared by all who faced it.", 500, 1400, heavy, admin),
                tank("BT-7", "A fast Soviet light tank designed for deep operations. Speed was its greatest weapon.", 200, 400, light, admin),
                tank("M24 Chaffee", "An American light tank combining speed with a respectable 75mm gun. The eyes of the armored division.", 220, 450, light, admin),
                tank("KV-1", "The Soviet heavy tank that shocked German forces in 1941. Nearly impervious to most anti-tank weapons.", 420, 1100, heavy, admin),
                tank("Cromwell", "Britain's fast cruiser tank. Exceptional speed made it ideal for exploitation and pursuit.", 350, 680, medium, admin),
                tank("T-70", "A compact Soviet light tank used for infantry support and reconnaissance missions.", 180, 380, light, admin),
                tank("Maus", "The heaviest tank ever built. A 188-ton German super-heavy prototype with unmatched armor.", 550, 1800, heavy, admin),
                tank("AMX 13 75", "A French post-war light tank featuring an innovative oscillating turret and autoloader.", 250, 420, light, admin)
        );

        tankRepository.saveAll(tanks);
    }

    private Tank tank(String name, String description, long power, long health, Category category, UserEntity owner) {
        return new Tank()
                .setName(name)
                .setDescription(description)
                .setPower(power)
                .setHealth(health)
                .setCategory(category)
                .setUser(owner)
                .setCreated(LocalDate.now());
    }
}

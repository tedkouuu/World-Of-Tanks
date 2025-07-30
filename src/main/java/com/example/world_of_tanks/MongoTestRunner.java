package com.example.world_of_tanks;

import com.example.world_of_tanks.models.dto.TankEventLog;
import com.example.world_of_tanks.mongoDbService.MongoEventLogger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Component
public class MongoTestRunner implements CommandLineRunner {

    @Override
    public void run(String... args) {
        MongoEventLogger logger = new MongoEventLogger();

        TankEventLog log = new TankEventLog(
                42L,
                "CREATE",
                LocalDateTime.now(),
                Map.of("name", "Tiger I", "type", "heavy", "armor", 100)
        );

        logger.logEvent(log);
    }
}

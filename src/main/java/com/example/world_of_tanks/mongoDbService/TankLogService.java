package com.example.world_of_tanks.mongoDbService;

import com.example.world_of_tanks.models.Tank;
import com.example.world_of_tanks.models.dto.TankEventLog;


import java.time.LocalDateTime;
import java.util.Map;

public class TankLogService {

    public static void log(String eventType, Tank tank) {
        if (tank == null) return;

        Map<String, Object> payload = Map.of(
                "name", tank.getName(),
                "health", tank.getHealth(),
                "power", tank.getPower(),
                "created", tank.getCreated().toString(),
                "category", tank.getCategory() != null ? tank.getCategory().getName() : null,
                "user", tank.getUser() != null ? tank.getUser().getUsername() : null
        );

        TankEventLog log = new TankEventLog(
                tank.getId(),
                eventType,
                LocalDateTime.now(),
                payload
        );

        new MongoEventLogger().addEvent(log);
    }
}

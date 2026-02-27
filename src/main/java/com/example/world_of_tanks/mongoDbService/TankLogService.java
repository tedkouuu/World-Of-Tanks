package com.example.world_of_tanks.mongoDbService;

import com.example.world_of_tanks.models.Tank;
import com.example.world_of_tanks.models.dto.TankEventLog;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class TankLogService {

    private static final MongoEventLogger logger = new MongoEventLogger();

    public static void log(String eventType, Tank tank) {
        if (tank == null) return;

        Map<String, Object> payload = new HashMap<>();
        payload.put("name", tank.getName());
        payload.put("health", tank.getHealth());
        payload.put("power", tank.getPower());
        payload.put("created", tank.getCreated().toString());
        payload.put("category", tank.getCategory() != null ? tank.getCategory().getName() : "unknown");
        payload.put("user", tank.getUser() != null ? tank.getUser().getUsername() : "unknown");

        TankEventLog logEntry = new TankEventLog(
                tank.getId(),
                eventType,
                LocalDateTime.now(),
                payload
        );

        CompletableFuture.runAsync(() -> {
            try {
                logger.addEvent(logEntry);
            } catch (Exception ignored) {
            }
        });
    }
}

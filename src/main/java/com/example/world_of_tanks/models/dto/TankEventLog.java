package com.example.world_of_tanks.models.dto;

import java.time.LocalDateTime;
import java.util.Map;

public class TankEventLog {
    private Long tankId;
    private String eventType;
    private LocalDateTime timestamp;
    private Map<String, Object> payload;

    public TankEventLog(Long tankId, String eventType, LocalDateTime timestamp, Map<String, Object> payload) {
        this.tankId = tankId;
        this.eventType = eventType;
        this.timestamp = timestamp;
        this.payload = payload;
    }

    public Long getTankId() {
        return tankId;
    }

    public String getEventType() {
        return eventType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }
}
package com.example.world_of_tanks.web;

import com.example.world_of_tanks.models.dto.TankEventDTO;
import com.example.world_of_tanks.services.ApacheKafkaProducerService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(
        value = "/api/kafka",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class KafkaRestController {

    private final ApacheKafkaProducerService kafkaProducerService;

    public KafkaRestController(ApacheKafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    @PostMapping("/send")
    public ResponseEntity<Map<String, String>> sendTankEvent(@RequestBody TankEventDTO event) {
        kafkaProducerService.sendTankEvent(event);
        return ResponseEntity
                .ok(Map.of("message", "Събитието е изпратено към Kafka!"));
    }
}

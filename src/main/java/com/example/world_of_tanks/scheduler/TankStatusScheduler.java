package com.example.world_of_tanks.scheduler;

import com.example.world_of_tanks.models.Tank;
import com.example.world_of_tanks.models.dto.TankEventDTO;
import com.example.world_of_tanks.services.ApacheKafkaProducerService;
import com.example.world_of_tanks.services.TankService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;

@Component
public class TankStatusScheduler {

    private final ApacheKafkaProducerService kafkaProducer;
    private final TankService tankService;

    public TankStatusScheduler(ApacheKafkaProducerService kafkaProducer,
                               TankService tankService) {
        this.kafkaProducer = kafkaProducer;
        this.tankService = tankService;
    }

    @Scheduled(fixedRate = 3_600_000)
    public void sendTankStatusUpdate() {
        List<Tank> allTanks = tankService.findAll();

        for (Tank tank : allTanks) {
            TankEventDTO event = new TankEventDTO(
                    tank.getName(),
                    tank.getUser().getUsername(),
                    tank.getCategory().getName().name(),
                    tank.getHealth(),
                    tank.getPower(),
                    tank.getCreated(),
                    "PERIODIC_TANK_STATUS",
                    LocalTime.now()
            );

            kafkaProducer.sendTankEvent(event);
        }

        System.out.printf("✔ Изпратени са %d Kafka събития за танкове.%n", allTanks.size());
    }
}

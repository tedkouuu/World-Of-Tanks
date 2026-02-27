package com.example.world_of_tanks.services;

import com.example.world_of_tanks.models.Tank;
import com.example.world_of_tanks.repositories.TankRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ScheduleService {

    private static final int EXTRA_HP_EVERY_DAY = 100;
    private final TankRepository tankRepository;

    public ScheduleService(TankRepository tankRepository) {
        this.tankRepository = tankRepository;
    }

    @Async("taskExecutor")
    @Transactional
    public void giveHpToAllTanks() {

        List<Tank> allTanks = this.tankRepository.findAll();

        if (allTanks.isEmpty()) {
            return;
        }

        allTanks.forEach(tank -> tank.setHealth(tank.getHealth() + EXTRA_HP_EVERY_DAY));

        this.tankRepository.saveAll(allTanks);
    }
}

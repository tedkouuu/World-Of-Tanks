package com.example.world_of_tanks.web;

import com.example.world_of_tanks.models.dto.TankAttackDTO;
import com.example.world_of_tanks.models.dto.TankDTO;
import com.example.world_of_tanks.services.TankService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Controller
public class HomeController {

    private final TankService tankService;
    private final Executor taskExecutor;

    public HomeController(TankService tankService,
                          @Qualifier("taskExecutor") Executor taskExecutor) {
        this.tankService = tankService;
        this.taskExecutor = taskExecutor;
    }

    @ModelAttribute("tankAttackDTO")
    public TankAttackDTO initBattle() {
        return new TankAttackDTO();
    }

    @GetMapping("/users/home")
    public String getHome(Model model, @AuthenticationPrincipal UserDetails userDetails) {

        String username = userDetails.getUsername();

        CompletableFuture<List<TankDTO>> ownFuture =
                CompletableFuture.supplyAsync(() -> tankService.getTanksOwnedBy(username), taskExecutor);

        CompletableFuture<List<TankDTO>> enemyFuture =
                CompletableFuture.supplyAsync(() -> tankService.getTanksOwnedByNot(username), taskExecutor);

        CompletableFuture<List<TankDTO>> sortedFuture =
                CompletableFuture.supplyAsync(() -> tankService.getAllSorted(), taskExecutor);

        CompletableFuture.allOf(ownFuture, enemyFuture, sortedFuture).join();

        model.addAttribute("ownTanks", ownFuture.join());
        model.addAttribute("enemyTanks", enemyFuture.join());
        model.addAttribute("sortedTanks", sortedFuture.join());

        return "home";
    }
}




















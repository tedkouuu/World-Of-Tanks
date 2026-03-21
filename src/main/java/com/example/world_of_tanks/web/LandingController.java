package com.example.world_of_tanks.web;

import com.example.world_of_tanks.models.dto.TankInfoDTO;
import com.example.world_of_tanks.services.TankService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class LandingController {

    private final TankService tankService;

    public LandingController(TankService tankService) {
        this.tankService = tankService;
    }

    @GetMapping("/")
    public String landing(Model model) {
        long tankCount = tankService.countTanks();
        List<TankInfoDTO> featured = tankService.findFeaturedTanks(3);

        model.addAttribute("tankCount", tankCount);
        model.addAttribute("featuredTanks", featured);

        return "index";
    }
}

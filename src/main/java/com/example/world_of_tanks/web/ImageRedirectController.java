package com.example.world_of_tanks.web;

import com.example.world_of_tanks.AWSS3.S3Service;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/assets/images")
public class ImageRedirectController {

    private final S3Service s3;
    private static final Map<String, String> LOCAL_FALLBACKS = Map.of(
            "login-icon.png", "login-icon.svg",
            "tank-icon.png", "tank-icon.svg",
            "search-icon.png", "search-icon.svg",
            "tank-attacker.jpg", "tank-attacker.svg",
            "tank-defender.jpg", "tank-defender.svg",
            "tank.jpg", "tank-hero.svg",
            "battle-bg.jpg", "battle-bg.svg"
    );
    public ImageRedirectController(S3Service s3) { this.s3 = s3; }

    @GetMapping("/{fileName:.+}")
    public String image(@PathVariable String fileName) {
        var key = "images/" + fileName;
        if (s3.isEnabled()) {
            try {
                if (s3.exists(key)) {
                    var url = s3.presignGet(key, 720);
                    return "redirect:" + url.toString();
                }
            } catch (Exception ignored) {
            }
        }
        return "redirect:/images/" + resolveLocalName(fileName);
    }

    private String resolveLocalName(String fileName) {
        return LOCAL_FALLBACKS.getOrDefault(fileName, fileName);
    }
}
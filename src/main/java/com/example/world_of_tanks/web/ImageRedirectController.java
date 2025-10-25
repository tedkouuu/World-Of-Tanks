package com.example.world_of_tanks.web;

import com.example.world_of_tanks.AWSS3.S3Service;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/assets/images")
public class ImageRedirectController {

    private final S3Service s3;
    public ImageRedirectController(S3Service s3) { this.s3 = s3; }

    @GetMapping("/{fileName:.+}")
    public String image(@PathVariable String fileName) {
        var url = s3.presignGet("images/" + fileName, 720);
        return "redirect:" + url.toString();   // <-- гарантиран 302
    }
}
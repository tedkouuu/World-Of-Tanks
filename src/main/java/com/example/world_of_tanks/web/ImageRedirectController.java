package com.example.world_of_tanks.web;

import com.example.world_of_tanks.AWSS3.S3Service;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

@Controller
@RequestMapping("/assets/images")
public class ImageRedirectController {

    private final S3Service s3;

    public ImageRedirectController(S3Service s3) {
        this.s3 = s3;
    }

    @GetMapping("/{fileName:.+}")
    public ResponseEntity<Void> get(@PathVariable String fileName) {
        var url = s3.presign("images/" + fileName, 720);
        return ResponseEntity.status(302)
                .location(URI.create(url.toString()))
                .build();
    }
}
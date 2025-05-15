package com.mohit.urlshortener.controller;

import com.mohit.urlshortener.service.UrlService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping
@AllArgsConstructor
@Slf4j
public class UrlController {

    private final UrlService urlService;

    @PostMapping("/api/v1/shorten")
    public ResponseEntity<String> shortenUrl(@RequestBody String longUrl) {
        longUrl = longUrl.replaceAll("=$", "");
        String shortCode = urlService.generateShortCode(longUrl);
        return ResponseEntity.ok("http://54.211.18.213/" + shortCode);
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode, HttpServletResponse response) throws IOException {
        String longUrl = urlService.getLongUrl(shortCode);
        response.sendRedirect(longUrl);
        return ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT).build();
    }

}

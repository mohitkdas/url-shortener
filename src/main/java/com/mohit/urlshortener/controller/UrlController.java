package com.mohit.urlshortener.controller;

import com.mohit.urlshortener.model.UrlEntity;
import com.mohit.urlshortener.repository.UrlRepository;
import com.mohit.urlshortener.service.UrlService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UrlController {

    private final UrlService urlService;
    private final UrlRepository urlRepository;

    @PostMapping("/shorten")
    public ResponseEntity<String> shortenUrl(@RequestBody String longUrl) {
        String shortCode = urlService.generateShortCode(longUrl);
        urlRepository.save(new UrlEntity(shortCode, longUrl, LocalDateTime.now()));
        return ResponseEntity.ok("http://54.211.18.213:80/" + shortCode);
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode, HttpServletResponse response) throws IOException {
        String longUrl = urlService.getLongUrl(shortCode);
        response.sendRedirect(longUrl);
        return ResponseEntity.status(HttpStatus.FOUND).build();
    }

}

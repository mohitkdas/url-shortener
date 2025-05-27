package com.mohit.urlshortener.controller;

import com.mohit.urlshortener.service.UrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping
@AllArgsConstructor
@Slf4j
@Tag(name = "URL Shortener", description = "APIs for URL shortening and redirection")
public class UrlController {

    private final UrlService urlService;

    @Operation(summary = "Shorten a long URL", description = "Generate a shortened URL from a long URL")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully shortened the URL")
    })
    @PostMapping("/api/v1/shorten")
    public ResponseEntity<String> shortenUrl(@RequestBody Map<String, String> payload) {
        String longUrl = payload.get("longUrl");
        log.info("Received long URL: {}", longUrl);
        String shortCode = urlService.generateShortCode(longUrl);
        return ResponseEntity.ok("http://54.211.18.213/" + shortCode);
    }

    @Operation(summary = "Redirect to long URL", description = "Redirects the shortened URL to the original long URL")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Successfully redirected"),
            @ApiResponse(responseCode = "404", description = "Short URL not found")
    })
    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode, HttpServletResponse response) throws IOException {
        log.info("Redirecting short code: {}", shortCode);
        String longUrl = urlService.getLongUrl(shortCode);
        response.sendRedirect(longUrl);
        return ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT).build();
    }

}

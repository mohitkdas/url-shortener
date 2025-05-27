package com.mohit.urlshortener.service;

import com.mohit.urlshortener.model.UrlEntity;
import com.mohit.urlshortener.repository.UrlRepository;
import com.mohit.urlshortener.util.Base62;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Slf4j
@Service
@AllArgsConstructor
public class UrlService {

    private UrlRepository urlRepository;
    private CounterService counterService;

    private RedisAdvancedClusterCommands<String, String> redisCommands;

    public String generateShortCode(String longUrl) {
        long nextId = counterService.getNextId();
        log.info("Decoded long URL: {}", longUrl);
        String shortCode = Base62.encode(nextId);
        urlRepository.save(new UrlEntity(longUrl, shortCode, LocalDateTime.now()));
        return shortCode;
    }

    public String getLongUrl(String shortCode) {
        String cachedUrl = redisCommands.get(shortCode);
        if (cachedUrl != null) {
            log.info("Cache hit for short code: {}", shortCode);
            return cachedUrl;
        }

        String longUrl = urlRepository.findByShortCode(shortCode)
                .map(UrlEntity::getLongUrl)
                .orElseThrow(() -> new RuntimeException("URL not found for: " + shortCode));

        log.info("Cache miss for short code: {}", shortCode);
        redisCommands.set(shortCode, longUrl);
        return longUrl;
    }
}

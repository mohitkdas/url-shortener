package com.mohit.urlshortener.service;

import com.mohit.urlshortener.model.UrlEntity;
import com.mohit.urlshortener.repository.UrlRepository;
import com.mohit.urlshortener.util.Base62;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class UrlService {

    private UrlRepository urlRepository;

    private RedisAdvancedClusterCommands<String, String> redisCommands;

    private static final long TTL_SECONDS = 60 * 60;

    public String generateShortCode(String longUrl) {
        String shortCode = Base62.encode(longUrl.hashCode());
        redisCommands.setex(shortCode, TTL_SECONDS, longUrl);
        urlRepository.save(new UrlEntity(longUrl, shortCode, LocalDateTime.now()));
        return shortCode;
    }

    public String getLongUrl(String shortCode) {
        String cachedUrl = redisCommands.get(shortCode);
        if (cachedUrl != null) {
            return cachedUrl;
        }

        return urlRepository.findByShortCode(shortCode)
                .map(UrlEntity::getLongUrl)
                .orElseThrow(() -> new RuntimeException("URL not found"));
    }
}

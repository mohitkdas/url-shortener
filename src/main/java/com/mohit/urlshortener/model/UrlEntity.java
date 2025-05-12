package com.mohit.urlshortener.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UrlEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String longUrl;
    private String shortCode;
    private LocalDateTime createdAt;

    public UrlEntity(String shortCode, String longUrl, LocalDateTime now) {
        this.longUrl = longUrl;
        this.shortCode = shortCode;
        this.createdAt = now;
    }
}

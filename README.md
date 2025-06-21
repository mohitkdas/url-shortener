# URL Shortener Service

A high-performance, distributed URL shortening service built with **Java**, **Spring Boot**, **PostgreSQL**, and **AWS ElastiCache Redis**, designed to support scalability, low-latency access, and robust deployment automation via **GitHub Actions** and **EC2**.

---

## 🚀 Features

- RESTful API to generate and resolve short URLs
- Read-through cache with Redis Cluster for high-speed lookups
- Atomic counter-based short code generation to ensure uniqueness
- Time-to-live (TTL) support for cache eviction
- Docker-compatible and EC2-ready deployment setup
- GitHub Actions pipeline for zero-downtime CI/CD

---

## 🧠 Engineering Decisions & Tradeoffs

### 1. Read-Through Cache with Redis (vs Write-Through or Hybrid)

**Decision:**  
Implemented a **read-through caching strategy** using AWS ElastiCache Redis Cluster to serve hot keys instantly.

**Why?**  
- URL lookups are extremely read-heavy; write-once, read-many.
- Read-through cache reduces PostgreSQL read load by ~60%.
- Automatically populates cache only on demand — avoids cache pollution from infrequently used URLs.

**Tradeoff:**  
- Slightly higher latency on first lookup (cold read).
- Requires fallback to the DB if Redis is down or data is evicted.

---

### 2. Counter-Based Base62 Short Code Generation

**Decision:**  
Used an **atomic Redis counter** to generate unique numeric IDs, which are then Base62 encoded into compact short codes.

**Why?**  
- Ensures **uniqueness** without costly hash collision checks.
- Base62 encoding yields shorter, more human-friendly URLs.
- Scalable: Counter can be partitioned or batched if needed.

**Tradeoff:**  
- Slightly coupled to Redis availability.
- Requires range reservation logic to avoid contention in a distributed setup.

---

### 3. Stateless Architecture with Redis + PostgreSQL

- Redis handles fast-access, ephemeral data (short code lookups).
- PostgreSQL acts as the persistent source of truth (audit, analytics, fallbacks).
- TTL ensures unused cache entries are evicted automatically.

---

## 🛠️ Tech Stack

| Layer             | Tool/Tech                              |
|-------------------|----------------------------------------|
| Backend           | Java, Spring Boot                      |
| Caching           | Redis (AWS ElastiCache Cluster)        |
| Database          | PostgreSQL                             |
| Deployment        | AWS EC2, GitHub Actions                |
| Load Handling     | Counter-based Base62 ID generation     |
| CI/CD             | GitHub Actions, SCP, SSH               |

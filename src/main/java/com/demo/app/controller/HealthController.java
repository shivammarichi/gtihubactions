package com.demo.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {

    /**
     * GET /api/health
     * Simple health check — useful for Docker health checks and load balancers.
     *
     * Example: curl http://localhost:8080/api/health
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
            "status",    "UP",
            "timestamp", LocalDateTime.now().toString(),
            "service",   "springboot-demo",
            "version",   System.getenv("APP_VERSION") != null
                            ? System.getenv("APP_VERSION") : "local-dev"
        ));
    }
}

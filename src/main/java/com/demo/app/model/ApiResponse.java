package com.demo.app.model;

import java.time.LocalDateTime;

public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;
    private String appVersion;

    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
        // Reads the APP_VERSION environment variable injected at runtime
        // (set during Docker build via ARG → ENV in Dockerfile)
        this.appVersion = System.getenv("APP_VERSION") != null
                ? System.getenv("APP_VERSION")
                : "local-dev";
    }

    // Getters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public T getData() { return data; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getAppVersion() { return appVersion; }
}

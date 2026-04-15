# ═══════════════════════════════════════════════════════════════════════════════
# STAGE 1 — BUILD
# ───────────────
# We use a full JDK + Maven image ONLY for building.
# This image is large (~500 MB) but it never goes into production.
# ═══════════════════════════════════════════════════════════════════════════════
FROM maven:3.9.6-eclipse-temurin-17 AS builder

# Set a working directory inside the container
WORKDIR /build

# ── Copy dependency descriptors first ──
# By copying pom.xml before the source code, Docker can cache the
# 'mvn dependency:go-offline' layer. If only source code changes,
# Docker reuses this cached layer → much faster subsequent builds.
COPY pom.xml .
RUN mvn dependency:go-offline -B

# ── Copy the actual source code ──
COPY src ./src

# ── Build: compile + test + package into a fat JAR ──
# -B          : batch mode (no color, less noise in CI logs)
# The fat JAR ends up at: target/springboot-demo-1.0.0.jar
RUN mvn clean package -B


# ═══════════════════════════════════════════════════════════════════════════════
# STAGE 2 — RUNTIME
# ──────────────────
# We use a minimal JRE image (no Maven, no JDK source).
# eclipse-temurin:17-jre-alpine is only ~85 MB — tiny and secure.
# ═══════════════════════════════════════════════════════════════════════════════
FROM eclipse-temurin:17-jre-alpine

# ── Build argument passed in at 'docker build' time ──
# GitHub Actions will pass: --build-arg APP_VERSION=abc1234
# Default fallback is "local" if not provided.
ARG APP_VERSION=local

# ── Convert the build arg into a runtime ENV variable ──
# Now the Spring Boot app can read: System.getenv("APP_VERSION")
ENV APP_VERSION=${APP_VERSION}

# Non-root user for security (never run apps as root inside containers)
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

WORKDIR /app

# ── Copy ONLY the fat JAR from the builder stage ──
# Nothing else from the build stage comes here — no source code, no Maven.
COPY --from=builder /build/target/springboot-demo-*.jar app.jar

# Expose the port Spring Boot listens on
EXPOSE 8080

# ── Health check ──
# Docker will call this every 30s to decide if the container is healthy.
HEALTHCHECK --interval=30s --timeout=5s --start-period=15s --retries=3 \
    CMD wget -qO- http://localhost:8080/api/health || exit 1

# ── Start the application ──
# Using exec form (array) — so signals (SIGTERM) go directly to the JVM,
# allowing graceful shutdown.
ENTRYPOINT ["java", "-jar", "app.jar"]

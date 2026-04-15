# Spring Boot Demo — GitHub Actions CI/CD

A minimal Spring Boot app with **2 GET APIs**, a multi-stage **Dockerfile**,
and a fully commented **GitHub Actions** workflow that tests, packages, and
pushes a Docker image on every push to `main`.

---

## API Endpoints

| Method | URL | Description |
|--------|-----|-------------|
| GET | `/api/products` | Return all products |
| GET | `/api/products/{id}` | Return one product by ID |
| GET | `/api/health` | Health check |

---

## Run Locally (without Docker)

```bash
mvn spring-boot:run
```

Then test:

```bash
curl http://localhost:8080/api/products
curl http://localhost:8080/api/products/1
curl http://localhost:8080/api/health
```

---

## Run Locally with Docker

```bash
# Build
docker build -t springboot-demo .

# Run
docker run -p 8080:8080 springboot-demo
```

---

## GitHub Actions Setup — Add Your Secrets

Go to your GitHub repo → **Settings → Secrets and variables → Actions → New repository secret**

| Secret Name | Value |
|-------------|-------|
| `DOCKER_USERNAME` | Your Docker Hub username |
| `DOCKER_PASSWORD` | A Docker Hub **Access Token** (not your password!) |

> Generate a Docker Hub Access Token at:
> https://hub.docker.com → Account Settings → Security → New Access Token

---

## Pipeline Flow

```
Push to main
    │
    ▼
┌─────────────────────┐
│  Job 1: Test        │  ← mvn test — fails fast if tests break
└────────┬────────────┘
         │ needs: test
         ▼
┌─────────────────────┐
│  Job 2: Build       │  ← mvn package → fat JAR → upload artifact
└────────┬────────────┘
         │ needs: build
         ▼
┌─────────────────────┐
│  Job 3: Docker      │  ← docker build → push to Docker Hub
└─────────────────────┘   (skipped on Pull Requests)
```

## Pull the Image Anywhere

After the pipeline runs:

```bash
docker pull yourusername/springboot-demo:latest
docker run -p 8080:8080 yourusername/springboot-demo:latest
```

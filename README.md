# Test Devsu Backend

![Podman](https://img.shields.io/badge/Podman-892CA0?style=for-the-badge&logo=podman&logoColor=white)

Welcome to the **Test Devsu Backend**. This repository contains the backend services for the Test Devsu UI, built with **Java**.

## 🚀 Getting Started

Follow these steps to get your development environment up and running.

### ⚙️ Configuration

Before running the application, create a container with the database

```
docker run -d --name devsu-postgres -e POSTGRES_DB=devsudb -e POSTGRES_USER=devsu -e POSTGRES_PASSWORD=1234 -p 5432:5432 postgres:16
```

### 🐳 Build & Run with Podman/Docker

We use Podman/Docker for containerization. You can build and run the environment with the following commands:

**1. Build the image**

VERSION: example 1.0.0

```bash
docker build -t test-devsu-be:{VERSION} .
```

**2. Run the container**

This command runs the container interactively, maps the current directory to `/app`, and exposes port `8080`.

```bash
docker run -d --name devsu-be -p 8080:8080 -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.containers.internal:5432/devsudb -e SPRING_DATASOURCE_USERNAME=devsu -e SPRING_DATASOURCE_PASSWORD=1234 test-devsu-be
```


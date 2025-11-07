# Task Manager API

## Overview
A **lightweight, production-ready REST API** for managing tasks. Built with **Spring Boot 3.5+** and **Java 21**, this service provides full CRUD operations with validation, error handling, and test coverage.

Key features:
- CRUD tasks
- MySQL database
- 100% unit and integration test, >80% coverage
- OpenAPI/Swagger UI documentation
- Dockerized deployment
- CI/CD ready (GitHub Actions + SonarQube)

## Architecture
| Layer              | File                                          | Responsibility (what the code does, with file reference)                                                                                                                                                                                                         |
| ------------------ | --------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Entry point**    | `TaskmanagerApplication.java`                 | Vanilla `@SpringBootApplication` class that bootstraps the entire Spring container and starts the embedded Tomcat server on port 8080.                                                                                                                           |
| **API Contract**   | `TasksApi.java` (interface)                   | OpenAPI-generator produces this interface; it declares the five REST endpoints (`GET /tasks`, `POST /tasks`, `GET /tasks/{id}`, `PUT /tasks/{id}`, `DELETE /tasks/{id}`) and their response contracts. |
| **Controller**     | `TasksApiController.java`                     | Generated stub that implements `TasksApi`; it does not contains business logic—it simply delegates to the `TasksApiDelegate` bean, keeping the REST layer thin.                                                                                                  |
| **Business Logic** | `TasksApiDelegateImpl.java`                   | Implements the logic; uses `TaskRepository` (Spring-Data JPA) to perform CRUD operations against **MySQL** and maps between JPA entities (`TaskEntity`) and OpenAPI DTOs (`Task`/`TaskRequest`).                                       |
| **Models**         | `Task.java`, `TaskRequest.java`, `Error.java` | OpenAPI-generator creates these POJOs; they are used as DTOs for JSON serialization and are returned/accepted by the REST layer.                                                                                                                                 |
| **Utilities**      | `ApiUtil.java`                                | Generated helper that writes example JSON responses when the delegate is not implemented (useful during scaffolding, currently unused because we provide a real implementation).                                                                                 |
| **Models**         | `Task.java`, `TaskRequest.java`, `Error.java` | OpenAPI-generator creates these POJOs; they are used as DTOs for JSON serialization and are returned/accepted by the REST layer.                                                                                                                                 |
| **Utilities**      | `ApiUtil.java`                                | Generated helper that writes example JSON responses when the delegate is not implemented.                                                                                 |
| **JPA Entity**     | `TaskEntity.java`                             | `@Entity` class mapped to the `tasks` table in MySQL.                                                                                             |
| **Data Access**    | `TaskRepository.java`                         | Spring-Data JPA repository interface provides CRUD methods (save, findById, findAll, deleteById, etc.) without requiring any implementation code.       

## Prerequisites

| Tool | Version | Required For |
|------|---------|--------------|
| **Java** | `21` (Temurin/OpenJDK) | Running the Spring Boot application |
| **Gradle** | `8.5+` (wrapper `gradlew` included) | Building and testing the project |
| **Docker** | `20.10+` | Running MySQL and the app in containers |
| **Docker Compose** | `v2.0+` | Orchestrating multi-container setup |
| **Git** | Any | Cloning the repository |

## Quick Start
```bash
# 1. Clone the repo or download the zip
git clone https://github.com/<repo name>
cd taskamanager

# 2. Run everything (API + MySQL)
./run.sh
```

## API Documentation
| Type | URL |
|------|-----|
| Swagger UI (interactive docs) | http://localhost:8080/swagger-ui.html |
| OpenAPI JSON (machine-readable) | http://localhost:8080/v3/api-docs |
| Postman Collection file | [postman_collection.json](postman-test-run.json) |

## Testing
**Strategy**:  
- **Unit Tests**: All logic tested in services.  
- **Integration Tests**: All endpoints tested.
- **93% Coverage**: All endpoints, validations, error paths, and edge cases tested.  
- **JaCoCo**: Generates HTML/XML reports.

### Unit Tests
```bash
./gradlew test
```
### Integration Tests
```bash
./gradlew integrationTest
```
## Database Schema
| Column        | Type            | Constraints                 | Description                 |
| ------------- | --------------- | --------------------------- | --------------------------- |
| `id`          | `BIGINT`        | PK                      | Unique task identifier      |
| `title`       | `VARCHAR(255)`  | NOT NULL                    | Short task headline         |
| `description` | `VARCHAR(1000)` | NOT NULL                    | Full task details           |
| `completed`   | `BOOLEAN`       | NOT NULL, DEFAULT FALSE     | Completion flag             |
| `created_at`  | `DATETIME(6)`   | NOT NULL, updatable = false | Row creation timestamp      |
| `updated_at`  | `DATETIME(6)`   | NOT NULL                    | Last modification timestamp |
Migration approach: SPRING_JPA_HIBERNATE_DDL_AUTO is update in docker-compose.yml, on first start, Hibernate will create / alter the schema in database automatically.

## CI/CD Pipeline
| File | `.github/workflows/test.yml` |
### Trigger strategy
- **Pull-request** to `develop`, `main`, `releases/**`, `hotfix/**`  
- **Push** to `main`
### Jobs & steps
1. **test** (runs on self-hosted Ubuntu runner)  
   1. Check out code  
   2. Set up JDK 21 (Temurin) with Gradle cache  
   3. Make `./gradlew` executable  
   4. Run `./gradlew clean test --info` (compiles + runs the 12 integration tests)  
   5. Always upload HTML & XML test reports as an artefact (7-day retention)
2. **MS-Teams** (optional, implemented but commented) notifications – success/failure for tests (needs webhook to msteams)
3. **SonarQube** (implemented but commented) – code-quality gate (needs sonarqube secrets)
### Artifacts
Download the zipped report from the **Actions** tab → **Click on the workflow you just ran** → **test-report-<run-number>**.  
Open `index.html` to view.
### How to enable
1. Push the file to `.github/workflows/test.yml` in the repo.  
2. Ensure the self-hosted runner is listening (setup instructions in **Settings** tab → **Actions** → **Runners** → **New self-hosted runner** ).  
3. Every PR/push now runs the full test-suite automatically.

## Assumptions Made
- **Java 21** and **Docker** are pre-installed on the machine that runs ./run.sh.
- **Port 8080** (application) and **3307** (MySQL) are free on the host.
- **Self-hosted GitHub runner** labelled self-hosted is available for the CI workflow; if absent, the test job must be changed to other available runners.

## Known Limitations
- **Authentication/authorisation** – the API is completely open (no auth); any client can create, update or delete tasks. Add JWT auth.
- **Hard-coded** configs or secrets in docker-compose.yml, should be externalised via secrets manager / Kubernetes secrets / Hashicorp Vault / other secured secrets storage services.
- **Hard-coded** configs or secrets in CI/CD pipeline, should be stored in GitHub **secrets and variables** section.
- **Rate-limiting or input sanitisation** beyond Bean-Validation annotations because malicious payloads could still hit the service. Rate limit endpoints to prevent over-use or DDoS or brute-force or oversized payload DoS controlling the API coming in.
- **Non-localhost and secured** – In production the service must not bind to localhost. The following example will be explain via cloud provider ,AWS services.
   1. VPC with public & private subnets.
   2. Amazon RDS (MySQL) in the private subnets.
   3. Application code packaged as a Docker image → pushed to Amazon ECR.
   4. Compute choices:
      1. EC2: run the container on an EC2 instance in a public subnet, security-group ingress only from the ALB.
      2. EKS: deploy the pod behind an AWS Load Balancer Controller.
      3. Lambda serverless.
   5. Application Load Balancer (ALB) in the public subnets, terminates TLS  with a certificate from AWS Certificate Manager (ACM).
   6. Route 53 public hosted zone: create an A (or CNAME) record that points the domain to the ALB DNS name.
   7. Security groups / NACLs:
      1. ALB → port 443/80 only.
      2. Backend → port 3306 only.
   8. Secrets: store DB password in AWS Secrets Manager and inject it at runtime, never hard-code in docker-compose.
- **Missing quality gate** - SonarQube scan (vulnerabilities, code-smells and coverage) before merge.
- **Missing notify** - pipeline succeeds/fails, notify developers (MS-Teams, Slack or e-mail alert)
- **No deployment job** - Deploy to for example, AWS ECR. Pipeline stops at test. Add rollback for bad release just in case (blue green etc)
- **No observability tools implemented yet** - grafana loki (logs), promethues (metrics), tempo (traces), notify developers for failures as well.
- **Endpoints** - versioning /v1/ prefix for compatibility (breaking changes can kill old clients).
- **Improve performance** - database indexing (faster reads), cache like Redis or Memcached (faster reads).
- **High Availability for production** - auto scaling (to prevent cases where pod is unavailable and server crashes), multi-AZ deployment, health probes (/health which is already implemented).

## Technology Stack
- Spring Boot 3.5+
- Java 21
- MySQL 8+
- Docker & Docker Compose
- Testcontainers (integration test)
- JUnit 5 + AssertJ (unit test)
- Gradle
- Github Action (CI/CD)

## Author
Wang Ming Shen - wangmingshen1@gmail.com

## Task Manager API – Example Requests & Responses

**Tested on:** `http://localhost:8080`  
**Task ID:** `8`  
**Total Time:** `45ms`  
**All 5 endpoints passed**

| # | Endpoint         | Method | Status           | Time  | Request                                                                 | Response                                                                 |
|---|------------------|--------|------------------|-------|-------------------------------------------------------------------------|--------------------------------------------------------------------------|
| 1 | `GET /tasks`     | `GET`  | `200 OK`         | 11ms  | ```http<br>GET http://localhost:8080/tasks```                           | ```json<br>[ { "id": 8, "title": "...", "description": "...", ... } ]``` |
| 2 | `POST /tasks`    | `POST` | `201 Created`    | 7ms   | ```http<br>POST http://localhost:8080/tasks<br>Content-Type: application/json<br><br>{ "title": "...", "description": "...", "completed": false }``` | ```json<br>{ "id": 8, "title": "...", "description": "...", ... }``` |
| 3 | `GET /tasks/8`   | `GET`  | `200 OK`         | 8ms   | ```http<br>GET http://localhost:8080/tasks/8```                         | ```json<br>{ "id": 8, "title": "...", "description": "...", ... }``` |
| 4 | `PUT /tasks/8`   | `PUT`  | `200 OK`         | 10ms  | ```http<br>PUT http://localhost:8080/tasks/8<br>Content-Type: application/json<br><br>{ "title": "Updated...", "completed": true }``` | ```json<br>{ "id": 8, "title": "Updated...", "completed": true, ... }``` |
| 5 | `DELETE /tasks/8`| `DELETE` | `204 No Content` | 9ms   | ```http<br>DELETE http://localhost:8080/tasks/8```                     | *(empty body)*                                                           |

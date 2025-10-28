#!/bin/bash
set -e

echo "Stopping any old containers..."
docker compose down 2>/dev/null || true

echo "Starting MySQL database..."
docker compose up -d db

echo "Waiting for MySQL to be ready..."
until docker exec taskmanager-mysql mysqladmin ping -h localhost --silent; do
  printf "."
  sleep 2
done
echo " MySQL ready!"

echo "Building Spring Boot JAR..."
./gradlew clean build -x test

echo "Starting application..."
docker compose up -d app

echo ""
echo "API is running!"
echo "   Swagger UI: http://localhost:8080/swagger-ui.html"
echo "   API Docs:   http://localhost:8080/v3/api-docs"
echo "   Health:     http://localhost:8080/actuator/health"
echo ""
echo "To stop: docker compose down -v"

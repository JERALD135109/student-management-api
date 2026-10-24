FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY . .
# Serve the existing frontend from Spring Boot and use its same-origin API.
RUN mkdir -p src/main/resources/static     && sed 's|http://localhost:8080/students|/students|g' frontend/index.html > src/main/resources/static/index.html     && ./mvnw clean package -DskipTests
EXPOSE 8080
CMD ["java", "-jar", "target/demo-0.0.1-SNAPSHOT.jar"]

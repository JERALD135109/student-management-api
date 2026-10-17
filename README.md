# Student Management System

A full-stack student records app: a Spring Boot REST API backed by MySQL, with a plain HTML/JavaScript frontend. The whole stack runs with one command using Docker Compose.

## Features

- Add, view, edit, and delete student records
- Validation: name and department are required, and errors are shown next to the field
- Clean error responses (for example, `404` with a readable message when a student does not exist)
- Data is stored in MySQL and survives container restarts (named Docker volume)

## Tech stack

| Layer | Technology |
|---|---|
| Backend | Java 21, Spring Boot, Spring Data JPA, Bean Validation |
| Database | MySQL 8.4 |
| Frontend | HTML, CSS, JavaScript (`fetch` API), served by nginx |
| Containers | Docker, Docker Compose |
| Testing | Postman collection (`postman/` folder), curl |

## Project structure

```
.
├── src/main/java/com/jerald/demo/   # Entity, repository, controller, exception handling
├── src/main/resources/              # application.properties
├── frontend/                        # index.html (UI served by nginx)
├── Dockerfile                       # Builds the Spring Boot app image
├── docker-compose.yml               # Runs app, database, and frontend together
└── postman/                         # Saved Postman requests
```

## Run it with Docker

Prerequisites: Docker Desktop (with WSL 2 integration on Windows).

1. Build the app image:

   ```bash
   docker build -t student-api .
   ```

2. Start all services:

   ```bash
   docker compose up -d
   ```

3. Open the app:
   - Frontend: http://localhost:8081
   - API: http://localhost:8080/students

4. Stop everything (data is kept in the `mysql-data` volume):

   ```bash
   docker compose down
   ```

   To also delete the database data, run `docker compose down -v`.

## API endpoints

Base URL: `http://localhost:8080/students`

| Method | Endpoint | Description | Success | Error |
|---|---|---|---|---|
| GET | `/students` | List all students | 200 | — |
| GET | `/students/{id}` | Get one student | 200 | 404 if not found |
| POST | `/students` | Add a student | 200 | 400 if validation fails |
| PUT | `/students/{id}` | Update a student | 200 | 404 if not found, 400 if validation fails |
| DELETE | `/students/{id}` | Delete a student | 200 | 404 if not found |

Example request body:

```json
{
  "name": "Priya",
  "department": "CSE"
}
```

Example validation error (400):

```json
{
  "name": "Name is required"
}
```

## Run without Docker

Requires Java 21 and a running MySQL 8.4 instance with a database called `studentdb`. Update `src/main/resources/application.properties` with your database user and password, then:

```bash
./mvnw spring-boot:run
```

## What I learned building this

- Spring Boot layers: controller (receives requests), repository (talks to the database), entity (maps to a table)
- Why `Integer` and not `int` for an auto-generated primary key
- Global exception handling with `@RestControllerAdvice`
- Validation with `@NotBlank` and `@Valid`
- Containers, Docker Compose networking (services reach each other by name), and named volumes for persistent data
- CORS, and why the browser needs the server to allow it

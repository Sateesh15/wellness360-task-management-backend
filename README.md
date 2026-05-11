# 📋 TaskFlow — Task Management System

> A clean, production-ready **RESTful Task Management API** built with Spring Boot 3, JWT authentication, and in-memory storage. No database setup required — runs out of the box.

---

## 🏗️ Project Structure

```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/wellness360/taskmanagement/
│   │   │   ├── TaskManagementApplication.java   # Spring Boot entry point
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java           # Login & Register endpoints
│   │   │   │   └── TaskController.java           # Task CRUD endpoints
│   │   │   ├── dto/
│   │   │   │   ├── AuthDto.java                  # Auth request/response payloads
│   │   │   │   └── TaskDto.java                  # Task request/response payloads
│   │   │   ├── model/
│   │   │   │   ├── Task.java                     # Task entity (in-memory)
│   │   │   │   └── TaskStatus.java               # Status enum: pending, in_progress, completed
│   │   │   ├── repository/
│   │   │   │   └── UserRepository.java           # In-memory user store
│   │   │   ├── security/
│   │   │   │   ├── JwtAuthFilter.java            # JWT request filter
│   │   │   │   ├── JwtUtil.java                  # Token generation & validation
│   │   │   │   └── SecurityConfig.java           # Spring Security + CORS config
│   │   │   ├── service/
│   │   │   │   ├── AuthService.java              # Authentication logic
│   │   │   │   └── TaskService.java              # Task business logic
│   │   │   └── exception/
│   │   │       └── GlobalExceptionHandler.java   # Centralized error handling
│   │   └── resources/
│   │       └── application.properties            # App configuration
│   └── test/
│       └── java/                                 # Unit & integration tests
├── pom.xml                                       # Maven dependencies
└── README.md
```

---

## 🚀 Tech Stack

| Layer          | Technology                              |
|----------------|-----------------------------------------|
| Framework      | Spring Boot 3.2.0                       |
| Language       | Java 17                                 |
| Security       | Spring Security + JWT (jjwt 0.11.5)     |
| API Docs       | SpringDoc OpenAPI (Swagger UI 2.3.0)    |
| Validation     | Jakarta Bean Validation                 |
| Build Tool     | Maven 3.8+                              |
| Storage        | In-memory (no database required)        |
| Utilities      | Lombok                                  |
| Test Coverage  | JaCoCo                                  |

---

## ✅ Prerequisites

- **Java 17+** — [Download](https://adoptium.net/)
- **Maven 3.8+** — [Download](https://maven.apache.org/download.cgi) *(or use the bundled `mvnw` wrapper)*

---

## ⚙️ Configuration

All configuration lives in `src/main/resources/application.properties`:

```properties
server.port=9095
spring.application.name=task-management

# JWT Configuration
jwt.secret=wellness360-task-management-super-secret-key-2024-minimum-32-chars
jwt.expiration=86400000    # 24 hours in milliseconds

# Swagger UI
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.enabled=true

# Logging
logging.level.com.wellness360=DEBUG
```

---

## 🏃 Running the Application

### Start the API Server

```bash
cd backend

# Linux / macOS
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

The API will be available at: **`http://localhost:9095`**

### Run Tests

```bash
cd backend

# Linux / macOS
./mvnw test

# Windows
mvnw.cmd test
```

JaCoCo coverage reports are generated at: `target/site/jacoco/index.html`

---

## 📖 Swagger / OpenAPI Documentation

Once the server is running, open in your browser:

```
http://localhost:9095/swagger-ui.html
```

Raw OpenAPI JSON spec:

```
http://localhost:9095/v3/api-docs
```

---

## 🔐 Authentication

TaskFlow uses **stateless JWT (Bearer Token) authentication**.

- Public routes: `/api/auth/**`, `/swagger-ui/**`, `/v3/api-docs/**`
- All `/api/tasks/**` routes **require** a valid `Authorization: Bearer <token>` header.
- Tokens expire after **24 hours** (configurable via `jwt.expiration`).
- Passwords are hashed using **BCrypt**.

### Auth Flow

```
1. Register  →  POST /api/auth/register  →  returns JWT token
2. Login     →  POST /api/auth/login     →  returns JWT token
3. Use token →  Add header: Authorization: Bearer <token>
```

---

## 📡 API Reference

### Base URL
```
http://localhost:9095
```

### Standard Response Format

All endpoints return a consistent response envelope:

```json
{
  "success": true,
  "message": "Human-readable message",
  "data": { }
}
```

---

### 🔑 Authentication APIs

#### `POST /api/auth/register` — Register a New User

Creates a new user account and returns a JWT token.

**Request Body:**
```json
{
  "username": "john_doe",
  "password": "securePassword123"
}
```

**Response `200 OK`:**
```json
{
  "success": true,
  "message": "Registration successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "username": "john_doe",
    "message": "Registration successful"
  }
}
```

---

#### `POST /api/auth/login` — Login

Authenticates a user and returns a JWT token.

**Request Body:**
```json
{
  "username": "john_doe",
  "password": "securePassword123"
}
```

**Response `200 OK`:**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "username": "john_doe",
    "message": "Login successful"
  }
}
```

**Error `401 Unauthorized`:**
```json
{
  "success": false,
  "message": "Invalid username or password"
}
```

---

### ✅ Task APIs

> **All task endpoints require JWT authentication.**
> Add header: `Authorization: Bearer <your-token>`

---

#### `GET /api/tasks` — Get All Tasks

Retrieves a list of all tasks.

**Response `200 OK`:**
```json
{
  "success": true,
  "message": "Retrieved 2 task(s)",
  "data": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "title": "Design database schema",
      "description": "Create ERD and define table relationships",
      "dueDate": "2026-05-20",
      "status": "pending",
      "createdAt": "2026-05-11T10:00:00",
      "updatedAt": "2026-05-11T10:00:00"
    }
  ]
}
```

---

#### `GET /api/tasks/{id}` — Get Task by ID

Retrieves a single task by its UUID.

**Path Parameter:** `id` — Task UUID

**Response `200 OK`:**
```json
{
  "success": true,
  "message": "Task retrieved",
  "data": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "title": "Design database schema",
    "description": "Create ERD and define table relationships",
    "dueDate": "2026-05-20",
    "status": "pending",
    "createdAt": "2026-05-11T10:00:00",
    "updatedAt": "2026-05-11T10:00:00"
  }
}
```

**Error `404 Not Found`:**
```json
{
  "success": false,
  "message": "Task not found with id: 550e8400-..."
}
```

---

#### `POST /api/tasks` — Create a New Task

Creates a new task. Status defaults to `pending`.

**Request Body:**

| Field         | Type     | Required | Constraints                                      |
|---------------|----------|----------|--------------------------------------------------|
| `title`       | `string` | ✅ Yes   | 1–200 characters                                 |
| `description` | `string` | ❌ No    | Max 1000 characters                              |
| `dueDate`     | `string` | ❌ No    | Format: `yyyy-MM-dd`, must be today or future    |

```json
{
  "title": "Design database schema",
  "description": "Create ERD and define all table relationships",
  "dueDate": "2026-05-20"
}
```

**Response `201 Created`:**
```json
{
  "success": true,
  "message": "Task created successfully",
  "data": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "title": "Design database schema",
    "description": "Create ERD and define all table relationships",
    "dueDate": "2026-05-20",
    "status": "pending",
    "createdAt": "2026-05-11T10:00:00",
    "updatedAt": "2026-05-11T10:00:00"
  }
}
```

---

#### `PUT /api/tasks/{id}` — Update a Task

Updates an existing task. All fields are optional — only provided fields are updated.

**Path Parameter:** `id` — Task UUID

**Request Body:**

| Field         | Type       | Required | Constraints                                       |
|---------------|------------|----------|---------------------------------------------------|
| `title`       | `string`   | ❌ No    | 1–200 characters                                  |
| `description` | `string`   | ❌ No    | Max 1000 characters                               |
| `dueDate`     | `string`   | ❌ No    | Format: `yyyy-MM-dd`                              |
| `status`      | `string`   | ❌ No    | One of: `pending`, `in_progress`, `completed`     |

```json
{
  "title": "Updated task title",
  "status": "in_progress",
  "dueDate": "2026-06-01"
}
```

**Response `200 OK`:**
```json
{
  "success": true,
  "message": "Task updated successfully",
  "data": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "title": "Updated task title",
    "description": "Create ERD and define all table relationships",
    "dueDate": "2026-06-01",
    "status": "in_progress",
    "createdAt": "2026-05-11T10:00:00",
    "updatedAt": "2026-05-11T12:30:00"
  }
}
```

---

#### `DELETE /api/tasks/{id}` — Delete a Task

Permanently deletes a task by its UUID.

**Path Parameter:** `id` — Task UUID

**Response `200 OK`:**
```json
{
  "success": true,
  "message": "Task deleted successfully",
  "data": null
}
```

**Error `404 Not Found`:**
```json
{
  "success": false,
  "message": "Task not found with id: 550e8400-..."
}
```

---

#### `PATCH /api/tasks/{id}/complete` — Mark Task as Complete

Shortcut endpoint to instantly mark a task as `completed`.

**Path Parameter:** `id` — Task UUID

**Response `200 OK`:**
```json
{
  "success": true,
  "message": "Task marked as complete",
  "data": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "title": "Design database schema",
    "description": "Create ERD and define all table relationships",
    "dueDate": "2026-05-20",
    "status": "completed",
    "createdAt": "2026-05-11T10:00:00",
    "updatedAt": "2026-05-11T14:00:00"
  }
}
```

---

## 📊 Task Status Values

| Status        | Value         | Description                     |
|---------------|---------------|---------------------------------|
| Pending       | `pending`     | Task not yet started (default)  |
| In Progress   | `in_progress` | Task currently being worked on  |
| Completed     | `completed`   | Task fully finished             |

---

## 🔒 Security & CORS

- **Authentication**: Stateless JWT via `Authorization: Bearer <token>` header
- **Password Hashing**: BCrypt
- **CORS**: All origins allowed (`*`) with credentials support
- **Allowed Methods**: `GET`, `POST`, `PUT`, `PATCH`, `DELETE`, `OPTIONS`
- **Allowed Headers**: `*`
- **Session Policy**: Stateless (no server-side sessions)

---

## 🧪 Quick Test with cURL

### 1. Register
```bash
curl -X POST http://localhost:9095/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username": "testuser", "password": "password123"}'
```

### 2. Login & Save Token
```bash
TOKEN=$(curl -s -X POST http://localhost:9095/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "testuser", "password": "password123"}' \
  | grep -o '"token":"[^"]*"' | cut -d'"' -f4)
```

### 3. Create a Task
```bash
curl -X POST http://localhost:9095/api/tasks \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"title": "My First Task", "description": "Testing the API", "dueDate": "2026-12-31"}'
```

### 4. Get All Tasks
```bash
curl -X GET http://localhost:9095/api/tasks \
  -H "Authorization: Bearer $TOKEN"
```

### 5. Mark Task as Complete
```bash
curl -X PATCH http://localhost:9095/api/tasks/{id}/complete \
  -H "Authorization: Bearer $TOKEN"
```

---

## 🗺️ API Endpoints Summary

| Method   | Endpoint                       | Auth Required | Description              |
|----------|-------------------------------|:-------------:|--------------------------|
| `POST`   | `/api/auth/register`          | ❌            | Register a new user      |
| `POST`   | `/api/auth/login`             | ❌            | Login and get JWT token  |
| `GET`    | `/api/tasks`                  | ✅            | Get all tasks            |
| `GET`    | `/api/tasks/{id}`             | ✅            | Get task by ID           |
| `POST`   | `/api/tasks`                  | ✅            | Create a new task        |
| `PUT`    | `/api/tasks/{id}`             | ✅            | Update a task            |
| `DELETE` | `/api/tasks/{id}`             | ✅            | Delete a task            |
| `PATCH`  | `/api/tasks/{id}/complete`    | ✅            | Mark task as complete    |
| `GET`    | `/swagger-ui.html`            | ❌            | Swagger UI               |
| `GET`    | `/v3/api-docs`                | ❌            | OpenAPI JSON spec        |

---

## 📦 Maven Dependencies

| Dependency                             | Purpose                        |
|----------------------------------------|--------------------------------|
| `spring-boot-starter-web`              | REST API / MVC                 |
| `spring-boot-starter-security`         | Security & authentication      |
| `spring-boot-starter-validation`       | Bean validation                |
| `jjwt-api / jjwt-impl / jjwt-jackson` | JWT token handling             |
| `springdoc-openapi-starter-webmvc-ui`  | Swagger UI & OpenAPI docs      |
| `lombok`                               | Boilerplate reduction          |
| `spring-boot-starter-test`             | Unit & integration testing     |
| `spring-security-test`                 | Security-aware test utilities  |
| `jacoco-maven-plugin`                  | Code coverage reports          |

---

## 👤 Author

**Wellness360 Team**  
Group ID: `com.wellness360` | Artifact: `task-management` | Version: `1.0.0`

---

*Built with ❤️ using Spring Boot 3 · Java 17 · JWT Authentication*
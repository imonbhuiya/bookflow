# 🏨 BookFlow — Hotel Booking Management API

[![BookFlow CI](https://github.com/imonbhuiya/bookflow/actions/workflows/ci.yml/badge.svg)](https://github.com/imonbhuiya/bookflow/actions/workflows/ci.yml)

BookFlow is a production-style hotel booking backend built with **Java 21 and Spring Boot**.

It provides REST APIs for user authentication, hotel and room management, and hotel bookings. The project includes JWT authentication, role-based authorization, booking availability validation, Flyway database migrations, automated testing, Swagger/OpenAPI documentation, and Docker-based deployment.

The project was built to demonstrate practical backend engineering concepts including layered architecture, relational database design, authentication and authorization, business-rule enforcement, testing, database migrations, and containerization.

---

## ✨ Features

### Authentication & Security

- User registration
- User login
- BCrypt password hashing
- JWT authentication
- Stateless Spring Security
- Role-based access control
- `USER` and `ADMIN` roles
- Protected API endpoints
- Ownership-based booking authorization

### Hotel Management

- Create hotels
- View hotels
- Update hotels
- Delete hotels
- Public hotel browsing
- Administrative hotel management

### Room Management

- Create rooms
- View rooms
- Update rooms
- Delete rooms
- Room capacity
- Room pricing
- Room types
- Room status management

Supported room types:

```text
SINGLE
DOUBLE
TWIN
DELUXE
SUITE
FAMILY
```

### Booking Management

- Create bookings
- View own bookings
- View individual bookings
- Update bookings
- Cancel bookings
- Administrative booking access
- Automatic price calculation
- Guest-capacity validation
- Room-status validation
- Date validation
- Prevention of overlapping bookings
- Ownership checks

Supported booking statuses:

```text
PENDING
CONFIRMED
CANCELLED
COMPLETED
```

### Infrastructure & Quality

- MySQL database
- Spring Data JPA / Hibernate
- Flyway database migrations
- DTO-based API layer
- Jakarta Bean Validation
- Global exception handling
- Swagger / OpenAPI
- Unit testing with JUnit and Mockito
- Spring Boot integration testing
- H2 test database
- Multi-stage Docker build
- Docker Compose
- Persistent MySQL Docker volume
- Environment-based configuration

---

## 🛠 Tech Stack

### Backend

- Java 21
- Spring Boot 4
- Spring Web MVC
- Spring Data JPA
- Hibernate
- Jakarta Validation
- Maven

### Security

- Spring Security
- JWT
- BCrypt
- Role-Based Access Control

### Database

- MySQL 8.4
- Flyway
- H2 for integration testing

### Testing

- JUnit
- Mockito
- Spring Boot Test
- MockMvc

### API Documentation

- Swagger UI
- OpenAPI

### DevOps

- Docker
- Docker Compose
- Git
- GitHub

---

## 🏗 Architecture

BookFlow uses a **feature-oriented layered architecture**.

```text
com.bookflow
│
├── auth
│   ├── controller
│   ├── dto
│   └── service
│
├── user
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── enums
│   ├── repository
│   └── service
│
├── hotel
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── enums
│   ├── repository
│   └── service
│
├── room
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── enums
│   ├── repository
│   └── service
│
├── booking
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── enums
│   ├── repository
│   └── service
│
├── security
│   ├── config
│   └── jwt
│
├── exception
├── config
│
└── BookflowApplication.java
```

A typical request follows:

```text
HTTP Request
     │
     ▼
Controller
     │
     ▼
Service
     │
     ├── Business Rules
     ├── Authorization
     └── Validation
     │
     ▼
Repository
     │
     ▼
JPA / Hibernate
     │
     ▼
MySQL
```

---

## 🧩 Domain Model

BookFlow contains four main domain entities:

```text
                    ┌───────────┐
                    │   HOTEL   │
                    └─────┬─────┘
                          │
                          │ 1
                          │
                          │ *
                    ┌─────▼─────┐
                    │   ROOM    │
                    └─────┬─────┘
                          │
                          │ 1
                          │
                          │ *
                    ┌─────▼─────┐
                    │  BOOKING  │
                    └─────▲─────┘
                          │ *
                          │
                          │ 1
                    ┌─────┴─────┐
                    │   USER    │
                    └───────────┘
```

### Relationships

```text
HOTEL  1 ───────────── * ROOM

USER   1 ───────────── * BOOKING

ROOM   1 ───────────── * BOOKING
```

Therefore:

- One hotel can contain many rooms.
- Each room belongs to one hotel.
- One user can create many bookings.
- Each booking belongs to one user.
- One room can have many bookings over time.
- Each booking reserves one room.

---

## 🔐 Authentication

BookFlow uses stateless JWT authentication.

```text
Client
   │
   │ email + password
   ▼
POST /api/auth/login
   │
   ▼
Authentication Service
   │
   ▼
Spring Security
   │
   ▼
JWT generated
   │
   ▼
Client sends:

Authorization: Bearer <token>
   │
   ▼
JWT Authentication Filter
   │
   ▼
Protected API
```

The JWT identifies the authenticated user, while the current user's authorities are loaded through Spring Security.

Passwords are stored using secure password hashing rather than plain text.

---

## 👮 Authorization

BookFlow supports two roles:

```text
USER
ADMIN
```

Newly registered accounts receive the `USER` role by default.

### USER

A normal authenticated user can perform operations such as:

- Browse hotels
- Browse rooms
- Create bookings
- View their own bookings
- Access their own booking
- Update their own booking
- Cancel their own booking

### ADMIN

An administrator can perform management operations including:

- Manage hotels
- Manage rooms
- View users
- Manage bookings
- Access bookings belonging to other users

Booking ownership is also validated in the service layer.

---

## 📅 Booking Business Rules

Booking operations enforce business rules on the server.

### Date Validation

The check-in date must occur before the check-out date.

```text
checkInDate < checkOutDate
```

Example:

```text
Check-in:   2026-10-10
Check-out:  2026-10-13

Result: Valid
```

An invalid range is rejected.

```text
Check-in:   2026-10-15
Check-out:  2026-10-12

Result: Rejected
```

### Guest Capacity

The requested number of guests cannot exceed the room capacity.

```text
Room capacity:      2
Requested guests:   4

Result: Rejected
```

### Room Status

Bookings can only be created for rooms that are available for booking.

Rooms can have statuses such as:

```text
ACTIVE
INACTIVE
MAINTENANCE
```

### Overlapping Bookings

BookFlow prevents overlapping active bookings for the same room.

```text
Existing booking:

10 Oct ─────────────── 15 Oct

Requested booking:

          13 Oct ─────────────── 17 Oct

          ↑ overlap ↑

Result: Rejected
```

Adjacent bookings are allowed:

```text
Booking A:

10 Oct ─────────────── 15 Oct

Booking B:

                       15 Oct ─────────────── 18 Oct

Result: Valid
```

Cancelled bookings do not block future availability.

### Price Calculation

The backend calculates the final booking price.

```text
Room price per night = €120
Number of nights     = 3

Total price:

€120 × 3 = €360
```

The client does not determine the authoritative final price.

---

## 🌐 API Overview

### Authentication

| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | `/api/auth/register` | Public | Register a new user |
| POST | `/api/auth/login` | Public | Authenticate and receive JWT |

### Hotels

| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/api/hotels` | Public | Get hotels |
| GET | `/api/hotels/{id}` | Public | Get hotel |
| POST | `/api/hotels` | ADMIN | Create hotel |
| PUT | `/api/hotels/{id}` | ADMIN | Update hotel |
| DELETE | `/api/hotels/{id}` | ADMIN | Delete hotel |

### Rooms

| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/api/rooms` | Public | Get rooms |
| GET | `/api/rooms/{id}` | Public | Get room |
| POST | `/api/rooms` | ADMIN | Create room |
| PUT | `/api/rooms/{id}` | ADMIN | Update room |
| DELETE | `/api/rooms/{id}` | ADMIN | Delete room |

### Bookings

| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/api/bookings` | ADMIN | Get all bookings |
| GET | `/api/bookings/me` | Authenticated | Get current user's bookings |
| GET | `/api/bookings/{id}` | Owner / ADMIN | Get booking |
| POST | `/api/bookings` | Authenticated | Create booking |
| PUT | `/api/bookings/{id}` | Owner / ADMIN | Update booking |
| DELETE | `/api/bookings/{id}` | Owner / ADMIN | Cancel booking |

### Users

User management endpoints are protected for administrators.

---

## 🗄 Database Migrations

BookFlow uses **Flyway** to manage the database schema.

Current migrations:

```text
V1 → Create users table
V2 → Create hotels table
V3 → Create rooms table
V4 → Create bookings table
```

On application startup:

```text
Spring Boot starts
       ↓
Flyway connects to MySQL
       ↓
Migration history checked
       ↓
Pending migrations applied
       ↓
Hibernate validates schema
       ↓
Application starts
```

Hibernate is configured to validate the schema rather than create it automatically.

---

## 🧪 Testing

BookFlow includes both unit and integration tests.

### Booking Service Unit Tests

`BookingServiceTest` uses JUnit and Mockito to test booking business logic independently of MySQL.

Test scenarios include:

- Booking creation
- Date validation
- Guest capacity
- Room status
- Overlapping bookings
- Price calculation
- Booking ownership
- Booking updates
- Booking cancellation
- Administrator access
- Missing resources

### Authentication Integration Tests

Integration tests verify:

- Successful registration
- Duplicate email rejection
- Request validation
- Successful login
- Invalid credentials

### Security Integration Tests

Security tests verify:

- Unauthenticated requests return `401`
- `USER` access to admin resources returns `403`
- `ADMIN` access to admin resources succeeds

### Current Test Suite

```text
BookingServiceTest                 23 tests
AuthControllerIntegrationTest       5 tests
SecurityIntegrationTest             3 tests
BookflowApplicationTests            1 test
────────────────────────────────────────────
Total                              32 tests
```

Current suite:

```text
Tests run: 32
Failures: 0
Errors: 0
Skipped: 0
```

Run all tests:

```bash
./mvnw test
```

Windows:

```powershell
.\mvnw.cmd test
```

Integration tests use an in-memory H2 database, allowing them to run independently from the development MySQL instance.

---

## 📖 Swagger / OpenAPI

BookFlow provides interactive API documentation through Swagger UI.

After starting the application, open:

```text
http://localhost:8080/swagger-ui/index.html
```

OpenAPI JSON is available at:

```text
http://localhost:8080/v3/api-docs
```

Protected endpoints support Bearer JWT authentication through Swagger.

Use the login endpoint to obtain a token and authorize Swagger before calling protected APIs.

---

# 🐳 Run with Docker Compose

The easiest way to run BookFlow is with Docker Compose.

The stack contains:

```text
Docker Compose
│
├── app
│   ├── BookFlow
│   ├── Spring Boot
│   └── Java 21
│
└── db
    ├── MySQL 8.4
    └── Persistent volume
```

Internally, BookFlow connects to MySQL using Docker's service DNS:

```text
BookFlow container
       │
       │ jdbc:mysql://db:3306/bookflow_db
       ▼
MySQL container
```

---

## 1. Prerequisites

Install:

- Git
- Docker
- Docker Compose

You do not need to install MySQL or Maven separately when using the Docker Compose setup.

---

## 2. Clone the Repository

```bash
git clone https://github.com/imonbhuiya/bookflow.git
cd bookflow
```

---

## 3. Configure Environment Variables

Copy the example environment configuration:

```bash
cp .env.example .env
```

Then configure `.env`:

```dotenv
DB_NAME=bookflow_db
DB_USERNAME=root
DB_PASSWORD=your_database_password
JWT_SECRET=your_base64_encoded_jwt_secret
```

Generate a suitable JWT secret, for example:

```bash
openssl rand -base64 32
```

Never commit `.env` to Git.

---

## 4. Start BookFlow

Build and start the complete stack:

```bash
docker compose up --build
```

Or run it in the background:

```bash
docker compose up -d --build
```

After startup:

```text
BookFlow API:
http://localhost:8080

Swagger:
http://localhost:8080/swagger-ui/index.html

MySQL host port:
localhost:3307
```

---

## 5. Check Containers

```bash
docker compose ps
```

Expected services:

```text
bookflow-app-1   Up
bookflow-db-1    Up (healthy)
```

---

## 6. Stop BookFlow

```bash
docker compose down
```

The MySQL named volume is preserved, so database data survives normal container recreation.

To intentionally remove the database volume as well:

```bash
docker compose down -v
```

> Warning: the `-v` option deletes the Compose-managed database volume and its stored data.

---

# 💻 Local Development

BookFlow can also run directly from Maven while MySQL runs locally or in Docker.

The application expects:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
JWT_SECRET
```

Example for macOS/Linux:

```bash
export DB_URL="jdbc:mysql://localhost:3307/bookflow_db"
export DB_USERNAME="root"
export DB_PASSWORD="your_database_password"
export JWT_SECRET="your_base64_encoded_jwt_secret"

./mvnw spring-boot:run
```

The exact database port depends on your local MySQL configuration.

---

## ⚙️ Configuration

Production/development credentials are not stored directly in `application.properties`.

The application reads configuration from environment variables:

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=validate

jwt.secret=${JWT_SECRET}
```

This allows configuration to change between:

```text
Local Development
Testing
Docker
Production
```

without changing application source code.

---

## 🐳 Dockerfile

BookFlow uses a multi-stage Docker build.

```text
Stage 1
Maven + JDK 21
      │
      ├── Download dependencies
      ├── Compile BookFlow
      └── Build JAR
             │
             ▼
Stage 2
Java 21 JRE
      │
      └── Run app.jar
```

This separates the build environment from the runtime environment.

---

## 💾 Docker Persistence

MySQL data is stored using a Docker named volume:

```text
bookflow_mysql_data
        │
        ▼
/var/lib/mysql
```

Therefore:

```text
docker compose down
        ↓
containers removed
        ↓
volume remains
        ↓
docker compose up
        ↓
database data remains
```

---

## 📌 Backend Engineering Concepts Demonstrated

BookFlow demonstrates:

- RESTful API development
- Feature-oriented layered architecture
- DTO separation
- Dependency injection
- JPA entity relationships
- Repository pattern
- Relational database modeling
- Database migrations
- Server-side validation
- Centralized exception handling
- Password hashing
- JWT authentication
- Role-based authorization
- Resource ownership authorization
- Business-rule enforcement
- Date-overlap detection
- Server-side price calculation
- Unit testing
- Mocking
- Integration testing
- API documentation
- Environment-based configuration
- Docker image creation
- Multi-stage Docker builds
- Docker networking
- Docker Compose
- Persistent database volumes

---

## 🚧 Future Improvements

Potential future improvements include:

- CI/CD pipeline
- Testcontainers
- Refresh tokens
- Pagination and filtering
- Advanced hotel/room search
- Booking concurrency protection
- Redis caching
- Email notifications
- Monitoring and metrics
- Cloud deployment

---

## 📄 License

This project is developed as a backend engineering portfolio and educational project.
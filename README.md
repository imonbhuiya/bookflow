# 📚 BookFlow

BookFlow is a backend REST API for hotel and room booking management, built with **Java and Spring Boot**.

The application provides a structured backend for managing users, hotels, rooms, and reservations. It follows a layered architecture with a focus on clean domain modeling, secure authentication, role-based authorization, booking availability, validation, and maintainable REST APIs.

> **Status:** BookFlow is currently under active development. See the [Development Status](#development-status) section for implemented and planned features.

---

## ✨ Features

### Currently Implemented

- Spring Boot application foundation
- Maven project configuration
- MySQL database connectivity
- Environment-based database configuration
- Core domain design
- User domain model
- User roles
- Gender enum
- Automatic entity timestamps

### Planned Features

- User registration and login
- Hotel management
- Room management
- Booking management
- Room availability checking
- Prevention of overlapping bookings
- Automatic booking price calculation
- Spring Security
- JWT authentication
- Role-based access control
- Request validation
- Global exception handling
- Flyway database migrations
- Swagger / OpenAPI documentation
- Unit and integration testing
- Docker support

---

## 🛠 Tech Stack

### Backend

- Java 21
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Hibernate
- Jakarta Validation

### Security

- Spring Security
- JWT Authentication
- Role-Based Access Control

### Database

- MySQL 8
- Flyway

### Testing

- JUnit
- Mockito
- Spring Boot Integration Testing

### Documentation

- Swagger
- OpenAPI

### Build & DevOps

- Maven
- Docker
- Git
- GitHub

---

## 🏗 Architecture

BookFlow follows a feature-oriented layered structure.

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

---

## 🧩 Domain Model

BookFlow is centered around four main domain entities:

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

This means:

- One hotel can contain many rooms.
- Each room belongs to one hotel.
- One user can create many bookings.
- Each booking belongs to one user.
- One room can have many bookings over time.
- Each booking reserves one room.

### Foreign Keys

```text
ROOM.hotel_id
     │
     └────────────→ HOTEL.id


BOOKING.user_id
        │
        └─────────→ USER.id


BOOKING.room_id
        │
        └─────────→ ROOM.id
```

---

## 👤 User Model

The User domain contains account, profile, authentication, authorization, and audit information.

```text
USER
──────────────────────────
id                  PK
first_name
last_name
email               UNIQUE
password
phone
date_of_birth
gender
street
city
postal_code
country
role
enabled
created_at
updated_at
```

### Roles

BookFlow supports two application roles:

```text
USER
ADMIN
```

New accounts receive the `USER` role by default.

The `ADMIN` role is intended for administrative operations such as managing hotels, rooms, users, and bookings.

### Gender

Supported values:

```text
MALE
FEMALE
OTHER
```

Enums are stored using their string representation rather than numeric ordinal values.

---

## 🏨 Hotel Model

The planned Hotel domain contains:

```text
HOTEL
──────────────────────────
id                  PK
name
description

street
city
postal_code
country

phone
email

star_rating
check_in_time
check_out_time

status

created_at
updated_at
```

One hotel can contain multiple rooms.

---

## 🚪 Room Model

The planned Room domain contains:

```text
ROOM
──────────────────────────
id                  PK
room_number
room_type
description

price_per_night
capacity
bed_count

status

created_at
updated_at

hotel_id            FK
```

Planned room types include:

```text
SINGLE
DOUBLE
TWIN
DELUXE
SUITE
FAMILY
```

Room prices will use `BigDecimal` in Java to avoid floating-point precision problems when working with monetary values.

---

## 📅 Booking Model

The planned Booking domain contains:

```text
BOOKING
──────────────────────────
id                  PK

check_in_date
check_out_date

number_of_guests

total_price
status

created_at
updated_at

user_id             FK
room_id             FK
```

Planned booking statuses:

```text
PENDING
CONFIRMED
CANCELLED
COMPLETED
```

---

## 🧠 Booking Business Rules

Booking creation will apply several business rules.

### Date Validation

The check-in date must be before the check-out date.

```text
checkInDate < checkOutDate
```

Example:

```text
Check-in:   2026-10-10
Check-out:  2026-10-13

Result: Valid
```

An invalid date range will be rejected.

```text
Check-in:   2026-10-15
Check-out:  2026-10-12

Result: Rejected
```

### Guest Capacity

The number of guests cannot exceed the selected room's capacity.

```text
Room capacity:     2
Requested guests:  4

Result: Rejected
```

### Room Availability

A room cannot have overlapping active reservations.

Example:

```text
Room 101

Existing booking:

10 Oct ───────────────── 15 Oct


Requested booking:

             13 Oct ───────────────── 17 Oct

             ↑ overlapping dates ↑

Result: Rejected
```

A new booking may begin on the checkout date of the previous booking:

```text
Booking A

10 Oct ─────────────── 15 Oct


Booking B

                       15 Oct ─────────────── 18 Oct

Result: Valid
```

### Price Calculation

The backend will calculate the booking price.

```text
Room price per night = €120
Number of nights     = 3

Total price:

€120 × 3 = €360
```

The client will not be trusted to determine the final booking price.

---

## 🔐 Security Design

BookFlow will use:

- Spring Security
- JWT authentication
- Password hashing
- Role-based authorization

Authentication will use the user's email as the login identifier.

```text
Client
   │
   │ email + password
   ▼
Authentication API
   │
   ▼
Spring Security
   │
   ▼
JWT
   │
   ▼
Protected API
```

Passwords will never be stored as plain text.

### Authorization

Two roles are planned:

```text
USER
ADMIN
```

Typical permissions:

| Operation | USER | ADMIN |
|---|:---:|:---:|
| Browse hotels | ✅ | ✅ |
| Browse rooms | ✅ | ✅ |
| Create booking | ✅ | ✅ |
| View own bookings | ✅ | ✅ |
| Cancel own booking | ✅ | ✅ |
| Create hotel | ❌ | ✅ |
| Update hotel | ❌ | ✅ |
| Manage rooms | ❌ | ✅ |
| View users | ❌ | ✅ |
| Manage bookings | ❌ | ✅ |

---

## 🌐 API Design

The REST API will be organized around the following resources:

```text
/api/auth
/api/hotels
/api/rooms
/api/bookings
/api/admin
```

### Planned Endpoints

| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | `/api/auth/register` | Public | Register user |
| POST | `/api/auth/login` | Public | Authenticate user |
| GET | `/api/hotels` | Public | Get hotels |
| GET | `/api/hotels/{id}` | Public | Get hotel details |
| GET | `/api/rooms` | Public | Search rooms |
| POST | `/api/bookings` | USER | Create booking |
| GET | `/api/bookings/me` | USER | Get current user's bookings |
| DELETE | `/api/bookings/{id}` | USER | Cancel booking |
| POST | `/api/admin/hotels` | ADMIN | Create hotel |
| POST | `/api/admin/rooms` | ADMIN | Create room |
| GET | `/api/admin/users` | ADMIN | Get users |
| GET | `/api/admin/bookings` | ADMIN | Get bookings |

> These endpoints represent the target API design and will be enabled as their corresponding features are implemented.

---

# 🚀 Getting Started

## Prerequisites

Before running BookFlow locally, install:

- Java 21 or newer
- Git
- MySQL 8+

The project includes the Maven Wrapper, so a separate Maven installation is not required.

Docker can optionally be used to run MySQL.

Verify Java:

```bash
java -version
```

---

## 1. Clone the Repository

```bash
git clone https://github.com/imonbhuiya/bookflow.git
```

Enter the project directory:

```bash
cd bookflow
```

---

## 2. Create the MySQL Database

Log in to MySQL:

```bash
mysql -u root -p
```

Create the database:

```sql
CREATE DATABASE bookflow_db;
```

Exit MySQL:

```sql
exit;
```

> Flyway migrations are planned for schema management. Until migrations are introduced, the database must exist before the application starts.

---

## 3. Configure Environment Variables

BookFlow does not store database credentials directly in the repository.

The application expects:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
```

Example:

```text
DB_URL=jdbc:mysql://localhost:3306/bookflow_db
DB_USERNAME=root
DB_PASSWORD=your_mysql_password
```

The application reads these values from `application.properties`:

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```

Never commit real database credentials to the repository.

---

## 4. Run BookFlow

### macOS / Linux

Export the required environment variables:

```bash
export DB_URL=jdbc:mysql://localhost:3306/bookflow_db
export DB_USERNAME=root
export DB_PASSWORD=your_mysql_password
```

Then run:

```bash
./mvnw spring-boot:run
```

### Windows PowerShell

```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/bookflow_db"
$env:DB_USERNAME="root"
$env:DB_PASSWORD="your_mysql_password"

.\mvnw.cmd spring-boot:run
```

When startup succeeds, BookFlow runs by default on:

```text
http://localhost:8080
```

---

# 🐳 Running MySQL with Docker

MySQL can be run in Docker instead of installing it directly on the host machine.

Make sure Docker is running, then execute:

```bash
docker run \
  --name bookflow-mysql \
  -e MYSQL_ROOT_PASSWORD=your_password \
  -e MYSQL_DATABASE=bookflow_db \
  -p 3307:3306 \
  -d mysql:8.4
```

This maps:

```text
Computer                  Docker Container

localhost:3307  ───────→  MySQL:3306
```

Configure BookFlow accordingly:

```text
DB_URL=jdbc:mysql://localhost:3307/bookflow_db
DB_USERNAME=root
DB_PASSWORD=your_password
```

Then start BookFlow:

```bash
./mvnw spring-boot:run
```

---

## 🧪 Running Tests

Run the test suite using:

```bash
./mvnw test
```

On Windows:

```bash
mvnw.cmd test
```

---

## 🔧 Configuration

BookFlow uses environment variables to keep environment-specific configuration outside the source code.

| Variable | Description | Example |
|---|---|---|
| `DB_URL` | MySQL JDBC connection URL | `jdbc:mysql://localhost:3306/bookflow_db` |
| `DB_USERNAME` | MySQL username | `root` |
| `DB_PASSWORD` | MySQL password | `your_password` |

This allows different configuration for:

```text
Local Development
Testing
Docker
Production
```

without changing the source code.

---

## 🗺 Development Status

### Project Foundation

- [x] Spring Boot project setup
- [x] Maven configuration
- [x] MySQL driver
- [x] MySQL connectivity
- [x] Environment-based database configuration
- [x] Git repository
- [x] GitHub repository

### Domain

- [x] Core domain design
- [x] User entity foundation
- [x] Gender enum
- [x] Role enum
- [x] User audit timestamps
- [ ] Hotel entity
- [ ] Room entity
- [ ] Booking entity
- [ ] Entity relationships

### Persistence

- [ ] Repository layer
- [ ] Flyway migrations
- [ ] Database constraints
- [ ] Booking availability queries

### REST API

- [ ] DTO layer
- [ ] User APIs
- [ ] Hotel APIs
- [ ] Room APIs
- [ ] Booking APIs
- [ ] Request validation
- [ ] Global exception handling

### Security

- [ ] Spring Security
- [ ] User registration
- [ ] User login
- [ ] Password hashing
- [ ] JWT authentication
- [ ] Role-based authorization

### Documentation & Testing

- [ ] Swagger / OpenAPI
- [ ] Unit tests
- [ ] Repository tests
- [ ] Integration tests

### Deployment

- [ ] Dockerfile
- [ ] Docker Compose
- [ ] Production configuration

---

## 📌 Project Goals

BookFlow is designed around several backend engineering principles:

- Clear separation of responsibilities
- RESTful API design
- Relational database modeling
- Secure authentication and authorization
- Server-side validation
- Business-rule enforcement
- Consistent exception handling
- Database migration management
- Automated testing
- Environment-based configuration
- Containerized deployment

---

## 📄 License

This project is intended for portfolio and educational purposes.
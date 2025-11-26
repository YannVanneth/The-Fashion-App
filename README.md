# 🛍️ The Fashion App - User/Auth Service

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.0-brightgreen)
![Java](https://img.shields.io/badge/Java-21-orange)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue)
![JWT](https://img.shields.io/badge/JWT-Enabled-red)

## 📋 Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Running with Docker](#running-with-docker)
- [API Endpoints](#api-endpoints)
- [Configuration](#configuration)
- [Database Schema](#database-schema)
- [Security](#security)
- [Testing](#testing)
- [Troubleshooting](#troubleshooting)

---

## 🎯 Overview

This is the **User/Authentication Service** for The Fashion App microservice architecture. It handles user registration, authentication, and JWT token management for the entire fashion e-commerce platform.

This service is responsible for:
- User registration and account management
- User authentication (login/logout)
- JWT token generation and validation
- Role-based access control (CUSTOMER, VENDOR, ADMIN)
- User favorites management

---

## ✨ Features

- ✅ **User Registration** - Register new users with email and password
- ✅ **User Authentication** - Secure login with JWT tokens
- ✅ **Role-Based Access Control** - Support for CUSTOMER, VENDOR, and ADMIN roles
- ✅ **Password Encryption** - BCrypt password hashing
- ✅ **JWT Token Management** - Access and refresh token support
- ✅ **PostgreSQL Database** - Persistent data storage
- ✅ **Docker Support** - Easy deployment with Docker Compose
- ✅ **Health Check Endpoint** - Monitor service status
- ✅ **User Favorites** - Track user favorite items
- ✅ **User Addresses** - Store multiple user addresses

---

## 🛠️ Tech Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 21 | Programming Language |
| Spring Boot | 4.0.0 | Application Framework |
| Spring Security | Latest | Authentication & Authorization |
| Spring Data JPA | Latest | Database ORM |
| PostgreSQL | 16 | Database |
| JWT (jjwt) | 0.12.5 | Token Generation |
| Lombok | Latest | Reduce Boilerplate Code |
| Gradle | Latest | Build Tool |
| Docker | Latest | Containerization |

---

## 📁 Project Structure

```
userService/
├── src/
│   ├── main/
│   │   ├── java/thefashion/authservice/
│   │   │   ├── UserServiceApplication.java     # Main Application
│   │   │   ├── config/                         # JWT & App Config
│   │   │   │   ├── JwtConfig.java
│   │   │   │   └── JwtUtils.java
│   │   │   ├── controller/                     # REST Controllers
│   │   │   │   ├── AuthController.java         # Auth endpoints
│   │   │   │   └── HealthController.java       # Health check
│   │   │   ├── domain/                         # Domain Models
│   │   │   │   ├── Role.java                   # User roles enum
│   │   │   │   ├── dto/                        # Data Transfer Objects
│   │   │   │   │   ├── AuthResponseDTO.java
│   │   │   │   │   ├── LoginRequestDTO.java
│   │   │   │   │   ├── LoginResponseDTO.java
│   │   │   │   │   ├── RegisterRequestDTO.java
│   │   │   │   │   └── CustomUserDetails.java
│   │   │   │   └── entity/                     # JPA Entities
│   │   │   │       ├── UserEntity.java
│   │   │   │       ├── UserAddressesEntity.java
│   │   │   │       └── UserFavoritesEntity.java
│   │   │   ├── exception/                      # Custom Exceptions
│   │   │   ├── repository/                     # JPA Repositories
│   │   │   ├── security/                       # Security Config
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   └── PasswordEnd.java
│   │   │   └── service/                        # Business Logic
│   │   │       ├── abstraction/
│   │   │       └── implement/
│   │   └── resources/
│   │       └── application.yml                 # App Configuration
│   └── test/
├── build.gradle                                # Gradle Dependencies
├── docker-compose.yml                          # Docker Configuration
└── README.md                                   # This file
```

---

## 🚀 Getting Started

### Prerequisites

Before running this service, ensure you have:

- ☕ **Java 21** or higher installed
- 🐘 **PostgreSQL 16** (or use Docker)
- 🐳 **Docker & Docker Compose** (optional, for containerized deployment)
- 📦 **Gradle** (included via wrapper)

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd userService
   ```

2. **Configure Database**
   
   Update `src/main/resources/application.yml` if needed:
   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/fashion_db
       username: fashionapp
       password: fashionapp123
   ```

3. **Build the project**
   ```bash
   ./gradlew clean build
   ```

4. **Run the application**
   ```bash
   ./gradlew bootRun
   ```

   The service will start on `http://localhost:8080` (default port)

---

### 🐳 Running with Docker

The easiest way to run this service is using Docker Compose:

1. **Start PostgreSQL database**
   ```bash
   docker-compose up -d
   ```

   This will:
   - Create a PostgreSQL 16 container
   - Set up the `fashion_db` database
   - Expose on port `5432`
   - Create a persistent volume for data

2. **Verify database is running**
   ```bash
   docker ps
   ```

3. **Run the Spring Boot application**
   ```bash
   ./gradlew bootRun
   ```

4. **Stop the database**
   ```bash
   docker-compose down
   ```

---

## 📡 API Endpoints

### Authentication Endpoints

#### 1. Register New User
```http
POST /api/v1/auth/register
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "password": "securePassword123",
  "role": "CUSTOMER"  // Optional: CUSTOMER, VENDOR (defaults to CUSTOMER)
}
```

**Response:**
```json
{
  "userId": "generated-uuid",
  "email": "john.doe@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "role": "CUSTOMER",
  "message": "User registered successfully"
}
```

#### 2. Login
```http
POST /api/v1/auth/login
Content-Type: application/json

{
  "email": "john.doe@example.com",
  "password": "securePassword123"
}
```

**Response:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer"
}
```

### Health Check Endpoint

#### Check Service Health
```http
GET /health
```

**Response:**
```json
{
  "status": "UP"
}
```

---

## ⚙️ Configuration

### Application Configuration (`application.yml`)

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/fashion_db
    username: fashionapp
    password: fashionapp123
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: create-drop  # Change to 'update' in production
    show-sql: true
    properties:
      hibernate:
        format_sql: true

jwt:
  secret-key: <base64-encoded-secret>
  expiration: 86400000        # 24 hours (milliseconds)
  refresh-expiration: 604800000  # 7 days (milliseconds)
```

### Important Configuration Notes:

⚠️ **For Production:**
- Change `ddl-auto` from `create-drop` to `update` or `validate`
- Use environment variables for sensitive data
- Generate a new secure JWT secret key
- Disable SQL logging (`show-sql: false`)

### Environment Variables (Recommended for Production)

```bash
export DB_URL=jdbc:postgresql://localhost:5432/fashion_db
export DB_USERNAME=fashionapp
export DB_PASSWORD=your-secure-password
export JWT_SECRET=your-base64-encoded-secret
export JWT_EXPIRATION=86400000
```

---

## 🗄️ Database Schema

### Users Table
```sql
CREATE TABLE users (
  id BIGSERIAL PRIMARY KEY,
  user_id VARCHAR(255) UNIQUE NOT NULL,
  fist_name VARCHAR(255) NOT NULL,
  last_name VARCHAR(255) NOT NULL,
  email VARCHAR(255) UNIQUE NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  role VARCHAR(50) NOT NULL DEFAULT 'CUSTOMER',
  status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
  created_at TIMESTAMP,
  updated_at TIMESTAMP
);



```

### User Roles
- `CUSTOMER` - Regular customers
- `VENDOR` - Product sellers/vendors
- `ADMIN` - System administrators

### Related Tables
- `user_addresses` - Stores user shipping/billing addresses
- `user_favorites` - Tracks user favorite products

---

## 🔐 Security

### JWT Authentication

This service uses JWT (JSON Web Tokens) for stateless authentication:

1. **Access Token**: Valid for 24 hours
2. **Refresh Token**: Valid for 7 days

### Password Security

- Passwords are hashed using **BCrypt** algorithm
- Plain text passwords are never stored
- Minimum password requirements should be enforced (add validation as needed)

### Security Headers

Spring Security is configured to provide:
- CSRF protection
- XSS protection
- Clickjacking protection
- Secure headers

---

## 🧪 Testing

### Run Tests
```bash
./gradlew test
```

### Test Coverage
```bash
./gradlew test jacocoTestReport
```

### Manual Testing with cURL

**Register a user:**
```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@example.com",
    "password": "password123",
    "role": "CUSTOMER"
  }'
```

**Login:**
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "password123"
  }'
```

**Health Check:**
```bash
curl http://localhost:8080/health
```

---

## 🐛 Troubleshooting

### Common Issues

#### 1. Database Connection Failed
```
Error: Could not connect to PostgreSQL
```
**Solution:**
- Ensure PostgreSQL is running: `docker-compose ps`
- Check database credentials in `application.yml`
- Verify port 5432 is not blocked

#### 2. Application Port Already in Use
```
Error: Port 8080 is already in use
```
**Solution:**
- Change the port in `application.yml`:
  ```yaml
  server:
    port: 8081
  ```

#### 3. JWT Token Errors
```
Error: Invalid JWT signature
```
**Solution:**
- Ensure the JWT secret key is properly configured
- Check token expiration time
- Verify the token format in Authorization header: `Bearer <token>`

#### 4. Gradle Build Failed
```
Error: Could not resolve dependencies
```
**Solution:**
- Check internet connection
- Clear Gradle cache: `./gradlew clean --refresh-dependencies`
- Ensure Java 21 is installed: `java -version`

---

## 📝 Notes for Your Partner

### Quick Start Guide

1. **Start the database:** `docker-compose up -d`
2. **Run the app:** `./gradlew bootRun`
3. **Test health:** `curl http://localhost:8080/health`
4. **Register a user:** Use the `/api/v1/auth/register` endpoint
5. **Login:** Use the `/api/v1/auth/login` endpoint to get JWT tokens

### Development Tips

- 🔄 **Auto-reload**: The service uses Spring DevTools for hot reload during development
- 📊 **SQL Logs**: Enable with `show-sql: true` in `application.yml`
- 🐛 **Debugging**: Run with `./gradlew bootRun --debug-jvm` for debugging
- 🧹 **Database Reset**: Current config uses `ddl-auto: create-drop` - database resets on restart

### Next Steps

To integrate with other microservices:
1. Use the JWT tokens from login response in Authorization headers
2. Format: `Authorization: Bearer <access-token>`
3. Validate tokens in other services using the same JWT secret

---

## 📞 Support

If you encounter any issues:
1. Check the [Troubleshooting](#troubleshooting) section
2. Review application logs in the console
3. Check Docker logs: `docker-compose logs -f`
4. Verify database connectivity

---

## 📄 License

This project is part of The Fashion App microservice architecture.

---

**Built with ❤️ for The Fashion App**


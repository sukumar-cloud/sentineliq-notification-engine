# SentinelIQ Notification Engine

A robust Spring Boot-based notification system with JWT authentication, Redis caching, email notifications, and comprehensive role-based access control (RBAC).

## Overview

The SentinelIQ Notification Engine is a production-ready backend service designed to manage user notifications with the following key features:

- **JWT Authentication**: Secure token-based authentication with refresh token support
- **Role-Based Access Control (RBAC)**: Admin and user roles with fine-grained permissions
- **Redis Caching**: High-performance caching with 10-minute TTL for GET operations
- **Email Notifications**: Scheduled daily reminders and deadline alerts using JavaMailSender and Thymeleaf
- **RESTful API**: Complete CRUD operations with pagination and validation
- **Docker Integration**: Fully containerized with 5 services including PostgreSQL, Redis, Mailhog, and Prometheus
- **Comprehensive Testing**: JUnit 5 tests with Mockito for service layer
- **Health Monitoring**: Spring Boot Actuator endpoints with Prometheus metrics

## Architecture

```
┌─────────────────┐
│   Client (Web)  │
└────────┬────────┘
         │ HTTP/HTTPS
         ▼
┌─────────────────────────────────────┐
│   Spring Boot Application          │
│  ┌───────────────────────────────┐  │
│  │   Controllers                │  │
│  │   - AuthController           │  │
│  │   - UserController           │  │
│  └───────────────┬───────────────┘  │
│                  │                   │
│  ┌───────────────▼───────────────┐  │
│  │   Services                   │  │
│  │   - AuthService              │  │
│  │   - UserService              │  │
│  │   - EmailService             │  │
│  │   - ScheduledEmailService    │  │
│  └───────────────┬───────────────┘  │
│                  │                   │
│  ┌───────────────▼───────────────┐  │
│  │   Repositories               │  │
│  │   - UserRepository           │  │
│  └───────────────┬───────────────┘  │
└──────────────────┼───────────────────┘
                   │
    ┌──────────────┼──────────────┐
    │              │              │
    ▼              ▼              ▼
┌─────────┐  ┌─────────┐  ┌──────────┐
│PostgreSQL│  │  Redis  │  │ Mailhog  │
└─────────┘  └─────────┘  └──────────┘
```

## Technology Stack

- **Backend**: Spring Boot 3.2.0, Java 17
- **Database**: PostgreSQL 15
- **Cache**: Redis 7
- **Authentication**: JWT (jjwt 0.11.5)
- **Email**: JavaMailSender, Thymeleaf
- **Testing**: JUnit 5, Mockito
- **Containerization**: Docker, Docker Compose
- **Monitoring**: Spring Boot Actuator, Prometheus
- **Build Tool**: Maven

## Prerequisites

Before running this application, ensure you have the following installed:

- Java 17 or higher
- Maven 3.6+
- Docker and Docker Compose
- Git

## Setup Steps

### 1. Clone the Repository

```bash
git clone https://github.com/sukumar-cloud/sentineliq-notification-engine.git
cd sentineliq-notification-engine
```

### 2. Using Docker Compose (Recommended)

Navigate to the backend directory and start all services:

```bash
cd backend/notification-engine
docker-compose up -d
```

This will start:
- PostgreSQL on port 5432
- Redis on port 6379
- Mailhog on ports 1025 (SMTP) and 8025 (Web UI)
- Application on port 8080
- Prometheus on port 9090

### 3. Using Maven (Local Development)

If you prefer to run locally without Docker:

1. Set up PostgreSQL and Redis locally
2. Configure environment variables (see .env reference below)
3. Build and run:

```bash
cd backend/notification-engine
mvn clean install
mvn spring-boot:run
```

### 4. Verify Installation

Check the health endpoint:

```bash
curl http://localhost:8080/actuator/health
```

## Environment Variables Reference

Create a `.env` file in `backend/notification-engine/` with the following variables:

| Variable | Description | Default | Required |
|----------|-------------|---------|----------|
| `DB_HOST` | Database host | localhost | Yes |
| `DB_PORT` | Database port | 5432 | Yes |
| `DB_NAME` | Database name | notification_db | Yes |
| `DB_USER` | Database username | postgres | Yes |
| `DB_PASSWORD` | Database password | postgres | Yes |
| `JWT_SECRET` | JWT signing secret | mySecretKey | Yes |
| `JWT_EXPIRATION` | JWT token expiration (ms) | 86400000 | No |
| `REDIS_HOST` | Redis host | localhost | Yes |
| `REDIS_PORT` | Redis port | 6379 | Yes |
| `MAIL_HOST` | SMTP server host | smtp.gmail.com | No |
| `MAIL_PORT` | SMTP server port | 587 | No |
| `MAIL_USERNAME` | SMTP username | - | No |
| `MAIL_PASSWORD` | SMTP password | - | No |

Example `.env` file:

```env
DB_HOST=localhost
DB_PORT=5432
DB_NAME=notification_db
DB_USER=postgres
DB_PASSWORD=postgres
JWT_SECRET=your-secret-key-here
JWT_EXPIRATION=86400000
REDIS_HOST=localhost
REDIS_PORT=6379
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password
```

## API Endpoints

### Authentication

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/auth/login` | Login with username and password |
| POST | `/auth/register` | Register a new user |
| POST | `/auth/refresh` | Refresh JWT token |

### User Management

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/users` | Get all users (paginated) | Yes |
| GET | `/users/{id}` | Get user by ID | Yes |
| GET | `/users/search` | Search users by keyword | Admin |
| POST | `/users` | Create new user | Admin |
| PUT | `/users/{id}` | Update user | Admin |
| DELETE | `/users/{id}` | Delete user | Admin |

### Health & Monitoring

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/actuator/health` | Application health status |
| GET | `/actuator/info` | Application information |
| GET | `/actuator/prometheus` | Prometheus metrics |

## Data Seeding

The application automatically seeds 30 users on first startup:
- 5 Admin users
- 25 Regular users
- All users have password: `password123`

## Testing

Run unit tests:

```bash
cd backend/notification-engine
mvn test
```

## Accessing Services

- **Application**: http://localhost:8080
- **Mailhog UI**: http://localhost:8025 (to view sent emails)
- **Prometheus**: http://localhost:9090 (to view metrics)

## Project Structure

```
backend/notification-engine/
├── src/
│   ├── main/
│   │   ├── java/com/internship/tool/
│   │   │   ├── config/          # Configuration classes
│   │   │   ├── controller/      # REST controllers
│   │   │   ├── dto/             # Data transfer objects
│   │   │   ├── entity/          # JPA entities
│   │   │   ├── exception/       # Custom exceptions
│   │   │   ├── filter/          # JWT filter
│   │   │   ├── repository/      # JPA repositories
│   │   │   ├── service/         # Business logic
│   │   │   └── util/            # Utility classes
│   │   └── resources/
│   │       ├── application.yml  # Application configuration
│   │       └── db/migration/    # Database migrations
│   └── test/                    # Unit tests
├── Dockerfile                   # Docker image definition
├── docker-compose.yml           # Multi-container orchestration
├── pom.xml                      # Maven dependencies
└── prometheus.yml               # Prometheus configuration
```

## Security Features

- Password encryption using BCrypt
- JWT token-based authentication
- Role-based access control (ADMIN, USER)
- CORS configuration
- SQL injection prevention via JPA
- Input validation on all endpoints

## Performance Features

- Redis caching with 10-minute TTL
- Database indexing on unique fields
- Pagination for large datasets
- Connection pooling
- Lazy loading for JPA entities

## Scheduled Tasks

- **Daily Reminder**: Runs at 9:00 AM daily
- **Deadline Alert**: Runs at 6:00 PM daily

## Troubleshooting

### Application won't start
- Check PostgreSQL and Redis are running
- Verify environment variables are set correctly
- Check application logs for errors

### Authentication failing
- Verify JWT_SECRET is set
- Check token expiration time
- Ensure user exists in database

### Emails not sending
- Verify Mailhog is running on port 1025
- Check SMTP configuration in application.yml
- View sent emails at http://localhost:8025

## License

This project is developed for educational purposes.

## Contact

For issues or questions, please open an issue on GitHub.
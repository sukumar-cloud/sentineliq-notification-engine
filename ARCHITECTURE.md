# SentinelIQ Notification Engine - Architecture

## High-Level Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    Client Applications                      │
│              (Web, Mobile, API Consumers)                   │
└────────────────────────┬────────────────────────────────────┘
                         │ HTTPS
                         ▼
┌─────────────────────────────────────────────────────────────┐
│              Spring Boot Application (Port 8080)            │
│  ┌──────────────────────────────────────────────────────┐  │
│  │              Security Layer                           │  │
│  │  - JWT Authentication Filter                         │  │
│  │  - Role-Based Access Control (RBAC)                  │  │
│  │  - CORS Configuration                                │  │
│  └──────────────────────────────────────────────────────┘  │
│                         │                                   │
│  ┌──────────────────────────────────────────────────────┐  │
│  │              REST Controllers                         │  │
│  │  - AuthController (login, register, refresh)         │  │
│  │  - UserController (CRUD, search, pagination)         │  │
│  └──────────────────────────────────────────────────────┘  │
│                         │                                   │
│  ┌──────────────────────────────────────────────────────┐  │
│  │              Service Layer                           │  │
│  │  - AuthService (authentication logic)                │  │
│  │  - UserService (business logic, validation)          │  │
│  │  - EmailService (JavaMailSender, Thymeleaf)         │  │
│  │  - ScheduledEmailService (daily reminders)           │  │
│  └──────────────────────────────────────────────────────┘  │
│                         │                                   │
│  ┌──────────────────────────────────────────────────────┐  │
│  │              Caching Layer                           │  │
│  │  - Redis Cache Manager (10 min TTL)                  │  │
│  │  - @Cacheable on GET operations                      │  │
│  │  - @CacheEvict on write operations                   │  │
│  └──────────────────────────────────────────────────────┘  │
│                         │                                   │
│  ┌──────────────────────────────────────────────────────┐  │
│  │              Data Access Layer                       │  │
│  │  - UserRepository (JPA Repository)                   │  │
│  │  - Custom query methods                              │  │
│  └──────────────────────────────────────────────────────┘  │
└────────────────────────┬────────────────────────────────────┘
                         │
        ┌────────────────┼────────────────┐
        │                │                │
        ▼                ▼                ▼
┌──────────────┐  ┌──────────┐  ┌─────────────┐
│ PostgreSQL   │  │  Redis   │  │ Mailhog     │
│ (Port 5432)  │  │(Port6379)│  │(Port 8025)  │
│              │  │          │  │             │
│ - Users      │  │ - Cache  │  │ - SMTP      │
│ - Auditing   │  │ - TTL    │  │ - Email UI  │
└──────────────┘  └──────────┘  └─────────────┘
```

## Technology Stack

### Backend
- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17
- **Build Tool**: Maven

### Database
- **Primary Database**: PostgreSQL 15
- **Cache**: Redis 7
- **ORM**: Spring Data JPA / Hibernate

### Security
- **Authentication**: JWT (jjwt 0.11.5)
- **Password Encryption**: BCrypt
- **Authorization**: Role-Based Access Control (RBAC)

### Email
- **SMTP**: JavaMailSender
- **Templates**: Thymeleaf
- **Testing**: Mailhog

### Monitoring
- **Health Checks**: Spring Boot Actuator
- **Metrics**: Prometheus
- **Logging**: SLF4J / Logback

### Testing
- **Unit Tests**: JUnit 5
- **Mocking**: Mockito
- **Coverage**: ~80%

### Containerization
- **Container**: Docker
- **Orchestration**: Docker Compose
- **Services**: 5 (App, PostgreSQL, Redis, Mailhog, Prometheus)

## Data Flow

### Authentication Flow
```
1. Client sends login request → AuthController
2. AuthService validates credentials → UserRepository
3. BCrypt password verification
4. JWT token generated → JwtUtil
5. Token returned to client
6. Client includes token in Authorization header
7. JwtAuthFilter validates token on each request
8. Security context set
9. Request proceeds to controller
```

### User CRUD Flow
```
1. GET /users → UserController
2. UserService checks cache (Redis)
3. If cached → return cached data
4. If not cached → query PostgreSQL
5. Save to cache (10 min TTL)
6. Return paginated results
```

### Email Notification Flow
```
1. User created/updated → UserService
2. EmailService.sendEmail() called
3. Thymeleaf template rendered
4. JavaMailSender sends via SMTP
5. Mailhog captures email (development)
6. Scheduled tasks send daily reminders
```

## Security Features

1. **Authentication**
   - JWT token-based authentication
   - Refresh token support
   - Token expiration (24 hours default)

2. **Authorization**
   - Role-based access control (ADMIN, USER)
   - @PreAuthorize annotations on endpoints
   - Fine-grained permissions

3. **Input Validation**
   - @Valid on all DTOs
   - Custom validation annotations
   - Consistent error responses

4. **Data Protection**
   - BCrypt password encryption
   - SQL injection prevention (JPA)
   - CORS configuration

5. **Auditing**
   - @CreatedDate, @LastModifiedDate
   - Automatic timestamp tracking
   - Audit trail for all records

## Performance Features

1. **Caching**
   - Redis for GET operations
   - 10-minute TTL
   - Cache eviction on writes

2. **Database Optimization**
   - Indexing on unique fields
   - Pagination for large datasets
   - Connection pooling

3. **Scalability**
   - Stateless REST API
   - Containerized deployment
   - Horizontal scaling ready

## Deployment Architecture

```
Production Environment:

┌─────────────────────────────────────────┐
│         Load Balancer (Nginx)           │
└────────────────┬────────────────────────┘
                 │
    ┌────────────┼────────────┐
    │            │            │
    ▼            ▼            ▼
┌────────┐  ┌────────┐  ┌────────┐
│ App 1  │  │ App 2  │  │ App 3  │
│(Docker)│  │(Docker)│  │(Docker)│
└───┬────┘  └───┬────┘  └───┬────┘
    │           │           │
    └───────────┼───────────┘
                │
    ┌───────────┼───────────┐
    ▼           ▼           ▼
┌────────┐  ┌────────┐  ┌────────┐
│PostgreSQL│ │  Redis │ │ SMTP   │
│Cluster  │ │ Cluster│ │ Server │
└────────┘  └────────┘  └────────┘
```

# Java Developer 1 Demo Script

## 90-Second Solo Presentation (Day 18)

**Problem Statement (10 seconds):**
"We built a secure, scalable notification engine to manage user authentication and notifications for enterprise applications."

**Architecture (20 seconds):**
"Spring Boot REST API with PostgreSQL for data persistence, Redis for high-performance caching, JWT for secure authentication, and JavaMailSender for scheduled email notifications. Fully containerized with Docker Compose."

**Key Features (40 seconds):**
"- JWT-based authentication with refresh tokens
- Role-based access control (Admin/User roles)
- Redis caching with 10-minute TTL for performance
- Scheduled email reminders (daily at 9 AM, deadline alerts at 6 PM)
- Comprehensive error handling with consistent JSON responses
- Health monitoring with Prometheus metrics
- 30 seeded users for testing
- Full test coverage with JUnit 5 and Mockito"

**Closing (20 seconds):**
"All services are containerized and production-ready with proper security, caching, and monitoring. The system handles authentication, user management, and notifications at scale."

---

## Day 19 - Live API Demo (Backend Focused)

### Prerequisites
```bash
cd backend/notification-engine
docker-compose up -d
```

### Demo Flow (5 minutes)

**1. Health Check (30 seconds)**
```bash
curl http://localhost:8080/actuator/health
```
- Show system is healthy
- All services running

**2. Authentication - Register (45 seconds)**
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "demouser",
    "email": "demo@example.com",
    "password": "password123"
  }'
```
- Show successful registration
- Note: JWT token returned

**3. Authentication - Login (45 seconds)**
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "demouser",
    "password": "password123"
  }'
```
- Show successful login
- Copy JWT token for next steps

**4. Get All Users with Pagination (45 seconds)**
```bash
curl http://localhost:8080/users?page=0&size=10 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```
- Show pagination working
- Note: 30 seeded users available

**5. Search Users (30 seconds)**
```bash
curl "http://localhost:8080/users/search?keyword=john&page=0&size=10" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```
- Show search functionality
- Only accessible by Admin

**6. Email Notification Demo (30 seconds)**
```bash
# Navigate to Mailhog UI
# Open http://localhost:8025 in browser
```
- Show sent emails
- Demonstrate email service integration

**7. Prometheus Metrics (30 seconds)**
```bash
# Navigate to Prometheus
# Open http://localhost:9090 in browser
```
- Show metrics collection
- Demonstrate monitoring capability

---

## Day 20 - Demo Day (6-minute presentation)

### Opening (30 seconds)
"Today I'm demonstrating the SentinelIQ Notification Engine - a secure, scalable backend system for user management and notifications."

### Architecture Slide (1 minute)
- Show architecture diagram from README
- Explain: Spring Boot → PostgreSQL → Redis → Email
- Highlight: Docker containerization for easy deployment

### Launch Live Tool (30 seconds)
```bash
docker-compose up -d
# Wait for services to start
curl http://localhost:8080/actuator/health
```
"All 5 services are now running: PostgreSQL, Redis, Mailhog, Application, and Prometheus"

### CRUD Demonstration (3 minutes)

**Create User:**
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"john","email":"john@test.com","password":"pass123"}'
```
"User created successfully with JWT token"

**Read Users:**
```bash
curl http://localhost:8080/users?page=0&size=5 \
  -H "Authorization: Bearer TOKEN"
```
"Fetching users with pagination - we have 30 seeded users"

**Update User:**
```bash
curl -X PUT http://localhost:8080/users/1 \
  -H "Authorization: Bearer TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"username":"john_updated","email":"john_updated@test.com","role":"ADMIN"}'
```
"User updated successfully"

**Delete User:**
```bash
curl -X DELETE http://localhost:8080/users/31 \
  -H "Authorization: Bearer TOKEN"
```
"User deleted - note this requires Admin role"

### Security Demo (1 minute)
```bash
# Without token - should return 401
curl http://localhost:8080/users

# With invalid input - should return 400
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"","email":"","password":""}'
```
"Proper authentication and validation - 401 for missing token, 400 for invalid input"

### Closing (30 seconds)
"The system is production-ready with comprehensive security, caching, email notifications, and monitoring. All code is tested, documented, and containerized."

---

## Talking Points for Q&A

**Q: How does Redis caching improve performance?**
A: We cache GET operations with a 10-minute TTL. This reduces database load by serving repeated requests from memory instead of querying PostgreSQL.

**Q: How is security implemented?**
A: JWT tokens for authentication, BCrypt for password encryption, role-based access control, input validation on all endpoints, and SQL injection prevention via JPA.

**Q: What happens if the email service fails?**
A: The application continues to run. Email failures are logged but don't block user operations. We use Mailhog for development to capture all emails.

**Q: How scalable is this system?**
A: PostgreSQL handles relational data efficiently, Redis provides high-speed caching, and the containerized architecture allows horizontal scaling. The pagination prevents loading all data at once.

**Q: How do you monitor the system?**
A: Spring Boot Actuator provides health endpoints, and Prometheus collects metrics for monitoring performance and uptime.

# Habit Tracker

A Spring Boot project for tracking daily habits, monitoring streaks, and analyzing completion statistics. Built with JWT-based authentication and PostgreSQL.

## Tech Stack

- **Framework**: Spring Boot 4.0.2 (Java 17)
- **Database**: PostgreSQL with Spring Data JPA
- **Auth**: JWT (HS256) via JJWT, Spring Security
- **Docs**: SpringDoc OpenAPI / Swagger UI
- **Build**: Gradle

## Getting Started

### Prerequisites

- Java 17+
- PostgreSQL running on `localhost:5432`
- A database named `habits_db`

### Run

```bash
./gradlew bootRun
```

The API starts on `http://localhost:8080`. Swagger UI is available at `/swagger-ui.html`.

### Configuration

Database connection and other settings are in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/habits_db
spring.datasource.username=<enter_postgres_username>
spring.datasource.password=<enter_postgres_password>
spring.jpa.hibernate.ddl-auto=update
```

## API Endpoints

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/auth/api/v1/register` | No | Register a new user |
| POST | `/auth/api/v1/login` | No | Login, receive JWT token |
| POST | `/auth/api/v1/logout` | Yes | Delete account |
| GET | `/api/v1/user/habits` | Yes | List all habits with current streaks |
| POST | `/api/v1/user/habits` | Yes | Create a new habit |
| POST | `/api/v1/user/habit/{id}/mark-done` | Yes | Mark a habit as done today |
| GET | `/api/v1/user/habit/{id}/history` | Yes | Get completion date history |
| GET | `/api/v1/user/habit/{id}/stats` | Yes | Get streak and completion stats |

## System Architecture

```
   Client (Expo / Web)
           │
           │  HTTP + JWT
           ▼
   ┌───────────────┐
   │  Controller   │
   └──────┬────────┘
          │
          ▼
   ┌───────────────┐
   │   Service     │
   └──────┬────────┘
          │
          ▼
   ┌───────────────┐
   │  Repository   │
   │    (JPA)      │
   └──────┬────────┘
          │
          ▼
     PostgreSQL DB
```

---

## Authentication 

```
Client                Controller            Service             DB            JWT
  │                      │                    │                  │             │
  │ POST /register       │                    │                  │             │
  ├────────────────────► │                    │                  │             │
  │                      ├─ registerUser() ─► │                  │             │
  │                      │                    ├─ save user ─────►│             │
  │                      │                    │◄─────────────────┤             │
  │◄──────────────────── │   200 OK           │                  │             │
  │                      │                    │                  │             │
  │ POST /login          │                    │                  │             │
  ├────────────────────► │                    │                  │             │
  │                      ├─ authenticate() ─► │                  │             │
  │                      │                    ├─ fetch user ────►│             │
  │                      │                    │◄─────────────────┤             │
  │                      ├─ generateToken ───────────────────────────────────►│
  │◄──────────────────── │   JWT Token        │                  │             │
```

---

## Request Authorization

```
Client            Auth Filter          JWT Service        Security Context     Controller
  │                   │                    │                     │                │
  │ Request + Token   │                    │                     │                │
  ├──────────────────►│                    │                     │                │
  │                   ├─ extractUsername ─►│                     │                │
  │                   │◄───────────────────┤                     │                │
  │                   ├─ validateToken ───►│                     │                │
  │                   │◄───────────────────┤                     │                │
  │                   │                                          │                │
  │                   ├─ setAuth ───────────────────────────────►│                │
  │                   ├────────────────────────────────────────►│                │
  │◄────────────────────────────────────────────────────────────┤   200 OK       │

  ❌ Invalid Token:
  │◄────────────────── 401 Unauthorized ─────────────────────────│
```

---

## Habit Tracking 

```
Client              Controller           Service                DB
  │                     │                   │                    │
  │ POST /habits/{id}/mark-done            │                    │
  ├────────────────────►│                   │                    │
  │                     ├─ markHabit() ───►│                    │
  │                     │                  ├─ find today's log ─►│
  │                     │                  │◄────────────────────┤
  │                     │                  │                    │
  │                     │                  ├─ create log ───────►│
  │                     │                  │◄────────────────────┤
  │◄────────────────────│   200 OK         │                    │

  ❌ Already marked:
  │◄────────────────────│   409 Conflict   │                    │
```

---

## Streak Calculation

```
          Fetch DONE logs
                 │
                 ▼
         Sort dates ascending
                 │
        ┌────────┴────────┐
        ▼                 ▼

 Current Streak     Longest Streak

     │                   │
     ▼                   ▼
Check today?        Iterate forward
     │                   │
 ┌───┴────┐         ┌────┴─────┐
 ▼        ▼         ▼          ▼
Yes      No     Consecutive?  Break
 │        │         │          │
 ▼        ▼         ▼          ▼
Start    Start   Increment    Reset
today   yesterday  streak     streak
     │                   │
     ▼                   ▼
Traverse back       Track max
until gap           streak
     │                   │
     ▼                   ▼
Return streak      Return max
```

---

## Stats Response

`GET /api/v1/user/habit/{id}/stats` returns:

```json
{
  "daysCompleted": 15,
  "daysMissed": 5,
  "longestStreak": 8,
  "currentStreak": 5
}
```

- **daysCompleted** — total days the habit was marked done
- **daysMissed** — days since habit creation minus days completed
- **longestStreak** — longest run of consecutive completions
- **currentStreak** — active consecutive streak from today

## Error Handling

All errors return a consistent format:

```json
{
  "timestamp": "2026-03-27T10:00:00",
  "message": "Habit not found"
}
```

| Status | Cause |
|--------|-------|
| 400 | Validation error |
| 401 | Invalid credentials or missing token |
| 404 | Habit not found |
| 409 | Duplicate entry (user or habit log) |
| 500 | Unexpected server error |

## Project Structure

```
src/main/java/com/niko/apps/
├── config/          # Security, JWT filter, beans
├── controllers/     # REST endpoints (auth, habits, tracking)
├── entity/          # JPA entities (User, Habit, HabitLog)
├── repository/      # Spring Data repositories
├── service/         # Business logic and JWT service
├── models/          # Request/response DTOs
├── utils/           # Streak calculation helpers
├── constants/       # Application constants
└── exceptions/      # Custom exception classes
```

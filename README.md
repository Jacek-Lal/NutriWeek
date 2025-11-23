# NutriWeek - Full-stack Meal Planner App

## About

NutriWeek is a full-stack web app for planning and tracking weekly nutrition.
It lets you:

- Build multi-day menus with multiple meals per day
- Attach real food products with macro data
- See how close each day is to your calorie and macro targets

The app is structured wih production-grade system in mind, with:

- Secure, session-based authentication and email verification
- Per-user data isolation
- Server-side and client-side validation
- Automated tests (JUnit 5 + Mockito)

---

## Live Demo

🔗 **App:** https://nutri-week.vercel.app/

**⚠️ Important note:**

> First request after a longer pause can be slow and take up few minutes because all services run on free tiers and need to warm up. Subsequent requests will be faster.

---

## Tech Stack

[![Java][Java.com]][Java-url]
[![Spring][Spring.com]][Spring-url]
[![JUnit][JUnit.com]][JUnit-url]

[![JavaScript][JavaScript.com]][JavaScript-url]
[![React][React.js]][React-url]
[![Tailwind][Tailwind.com]][Tailwind-url]
[![Bootstrap][Bootstrap.com]][Bootstrap-url]

[![Postgres][Postgres.com]][Postgres-url]
[![Docker][Docker.com]][Docker-url]

**Backend:** Java 21 · Spring Boot 3 · Spring Security 6 · Spring Data JPA · PostgreSQL  
**Frontend:** JavaScript · React · React Router · Axios · TailwindCSS  
**DevOps:** Docker · Docker Compose  
**Testing:** JUnit 5 · Mockito · H2 (integration tests)  
**Hosting:** Vercel (Frontend) · Render (Backend) · Neon (Database) · Brevo (email API)

---

## Key Features

- **User Accounts & Auth**

  - Registration with email verification link
  - Login with session-based authentication and CSRF protection
  - Demo account login (one-click) with temporary data that gets cleaned up automatically

- **Menus & Meals**

  - Create menus with configurable number of days, meals per day, macro nutrients ratio and meal calories ratio
  - Add meals and products to each day
  - Automatically calculates total macros (protein / fat / carbs) per meal and per day

- **Products & Nutrition**

  - Search products via USDA FoodData Central API
  - Store selected products in the database and reuse them across menus

---

## Technical Highlights

- **Security**

  - Session-based auth (`JSESSIONID`)
  - CSRF protection using `CookieCsrfTokenRepository` and `X-XSRF-TOKEN` header
  - CORS configured to allow only the React frontend origins (local + production)

- **Architecture**

  - Clear separation between controller, service, repository and DTO layers
  - Centralized exception handling with `@ControllerAdvice` and custom error responses

- **Background Jobs**

  - Spring `@Scheduled` job that deletes expired verification tokens and unverified/demo users

- **Emails**

  - Email verification implemented via Brevo email API
  - Tokenized verify link and redirect back to frontend with proper status (`success`, `expired`, etc.)

- **Dockerized Dev & Prod**

  - `docker-compose.dev.yml` – hot-reload dev setup (Maven `spring-boot:run` + `npm start`)
  - `docker-compose.yml` – production-like setup (built JAR + built React served by Nginx)
  - Dedicated `Dockerfile` for backend and frontend, plus `application-dev.yml` / `application-prod.yml`

- **Testing**
  - JUnit 5 + Mockito for unit tests
  - Service-layer tests (e.g. `AuthService`, `MenuService`, `MealService`, `EmailService`)

---

## Getting Started

### 1. Prerequisites

- Docker & Docker Compose
- USDA API key (FoodData Central)
- Brevo API key (for email verification) – or disable mail locally if you don’t care

### 2. Clone the repo

```bash
git clone https://github.com/Jacek-Lal/NutriWeek.git
cd NutriWeek
```

### 3. Environment variables

Create a .env file in the project root (same level as docker-compose.yml), for example:

```
# Database

DB_URL=jdbc:postgresql://<host>:<port>/<database>
DB_USER=<your_db_username>
DB_PASSWORD=<your_db_password>

# Frontend URLs

FRONTEND_URL=http://localhost:3000
FRONTEND_VERIFIED_URL=http://localhost:3000/login
USER_VERIFY_URL=http://localhost:8080/auth/verify

# API base URL for frontend container

REACT_APP_API_URL=http://localhost:8080

# External APIs / email

USDA_API_KEY=your_usda_key_here
BREVO_API_KEY=your_brevo_key_here
SENDER_EMAIL=your_email

# CSRF cookie domain (for prod host)

COOKIE_DOMAIN=localhost
```

For production deployment, these values are set in Render / Vercel / Neon dashboards and adjusted to real domains.

### 4. Run in development mode (recommended)

This gives you hot reload on backend and frontend.
Make sure to enable hot reloading in your IDE.

```
docker compose -f docker-compose.dev.yml up --build
```

Backend: http://localhost:8080 \
Frontend: http://localhost:3000

**⚠️ Important note:**

> Hot reloading tested only for Linux with docker-compose plugin. Windows setup, especially with Docker Desktop may require additional configuration.

### 5. Run production-like locally

Build and run the production Docker images (Spring Boot JAR + React build served by Nginx):

```
docker compose up --build
```

Backend: http://localhost:8080 \
Frontend: http://localhost:3000

## Roadmap

### Possible additions and improvements

- Drag & Drop feature for meals and products

- Custom products

- Advanced product search & filtering

- User profile & settings (preferred macros, units, etc.)

- Analytics (weekly summaries, charts)

- Admin tools (e.g. manage global products)

- More test coverage (integration, E2E tests)

- CI/CD pipeline (GitHub Actions)

[Bootstrap.com]: https://img.shields.io/badge/Bootstrap-563D7C?style=for-the-badge&logo=bootstrap&logoColor=white
[Bootstrap-url]: https://getbootstrap.com
[React.js]: https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB
[React-url]: https://reactjs.org/
[Postgres.com]: https://img.shields.io/badge/postgresql-4169e1?style=for-the-badge&logo=postgresql&logoColor=white
[Postgres-url]: https://www.postgresql.org/
[Java.com]: https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white
[Java-url]: https://www.java.com/
[Spring.com]: https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white
[Spring-url]: https://spring.io/
[JUnit.com]: https://img.shields.io/badge/JUnit5-f5f5f5?style=for-the-badge&logo=junit5&logoColor=dc524a
[JUnit-url]: https://junit.org/
[Tailwind.com]: https://img.shields.io/badge/tailwindcss-%2338B2AC.svg?style=for-the-badge&logo=tailwind-css&logoColor=white
[Tailwind-url]: https://v3.tailwindcss.com/
[JavaScript.com]: https://img.shields.io/badge/javascript-%23323330.svg?style=for-the-badge&logo=javascript&logoColor=%23F7DF1E
[JavaScript-url]: https://developer.mozilla.org/en-US/docs/Web/JavaScript
[Docker.com]: https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white
[Docker-url]: https://www.docker.com/

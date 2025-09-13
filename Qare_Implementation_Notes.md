# Qare — Implementation Notes

This document captures the key decisions and steps taken to build **Qare**, along with concise explanations you can refer to during maintenance or demos.

---

## 1.1 Backend (Java + Maven + Spring Boot + JUnit Jupiter)

### Scaffold (via Spring Initializr)
```bash
mkdir qare && cd qare
mkdir backend && cd backend

curl https://start.spring.io/starter.zip \
  -d name=qare-backend \
  -d groupId=com.qare \
  -d artifactId=qare-backend \
  -d packageName=com.qare.app \
  -d language=java \
  -d type=maven-project \
  -d javaVersion=24 \
  -d bootVersion=3.5.5 \
  -d dependencies=web,validation,actuator,lombok \
  -o starter.zip

unzip starter.zip && rm starter.zip
```

> We’re using **JUnit 5 (Jupiter)** for tests via `spring-boot-starter-test`.

---

### Architecture (Backend)

#### **Config**
- **`CorsConfig`**  
  Tells Spring which **browser origins** are allowed to call the API (CORS).  
  - CORS affects **browsers only**; tools like `curl`/Postman ignore it.  
  - **CORS is *not* authentication.** Even with correct CORS, your endpoints are open unless you add auth (e.g., Spring Security + JWT/cookies).  
  - **If you don’t use JWT/auth:** all endpoints are publicly reachable. CORS won’t stop malicious sites from *their own* users calling your API if you rely on cookies—use token-based auth or CSRF protection for cookie-based auth.

- **`DBConfig`**  
  Handles the database connection and provides CRUD operations. We add dependencies for **JDBC** and **H2**.
  - **JDBC (Java Database Connectivity):** standard Java API used by drivers (H2/Postgres/etc.) for SQL access.
  - **H2 (file-based):** great for local dev/tests; DB is created on first connection.
  - Switching to **PostgreSQL** for prod is trivial—**swap the dependency and datasource URL**; the CRUD code still works.

  **Swap H2 → Postgres in `pom.xml`:**
  ```xml
  <!-- remove H2 dependency -->
  <!-- add Postgres -->
  <dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
  </dependency>
  ```

  **`application.yml` for Postgres:**
  ```yaml
  spring:
    datasource:
      url: jdbc:postgresql://localhost:5432/qaredb
      username: postgres
      password: yourpassword
  ```

#### **Controller**
- **`QareController`**  
  The app’s **HTTP adapter**: turns incoming HTTP requests into service calls, then shapes the HTTP responses.

  **Where Jackson fits in (serialization/deserialization):**
  - `spring-boot-starter-web` pulls in `spring-boot-starter-json` → **Jackson** (`jackson-databind`, etc.).
  - Spring MVC registers `MappingJackson2HttpMessageConverter` with a Jackson **`ObjectMapper`**.
  - Returning `MedicalSupply` (or `List<MedicalSupply>`) from a `@RestController` method lets Spring’s message converter call Jackson to **serialize** to JSON.  
  - `@RequestBody MedicalSupply` does the reverse—Jackson **deserializes** JSON into your model.

  **Pipeline:**  
  `Controller return → MessageConverter → ObjectMapper.writeValue(...) → JSON response`

#### **Model**
- **`MedicalSupply`** (record): holds business fields and bean validation annotations, e.g.:
  ```java
  public record MedicalSupply(
      @jakarta.validation.constraints.NotBlank String name,
      @jakarta.validation.constraints.Min(0) int amount,
      @jakarta.validation.constraints.NotBlank String unitName
  ) {}
  ```

#### **Service**
- **`QareService`**  
  Orchestrates operations between controller and DB: input normalization (e.g., `strip()`/`trim()`), CRUD delegation, and (optionally) transactions.

#### **Resources**
- **`application.yml`**  
  Externalizes configuration (port, datasource, logging, actuator). Spring reads it at startup; values can be overridden by env vars/CLI args/profiles.

---

### Testing (Backend)

- **Controller tests**: `@WebMvcTest(QareController.class)` + mock service; assert status codes (201/200/404/204), headers (Location), JSON bodies, and validation 400s.  
- **DBConfig tests**: `@JdbcTest` + H2; verify table creation at startup, CRUD operations, PK violation, and DB `CHECK (amount >= 0)` constraint.  
- **Service tests**: Mockito unit tests; verify normalization and exception propagation (no blanket catching).  
- **Jupiter** is the platform (JUnit 5); AssertJ for fluent assertions.

---

## 1.2 Frontend (React + Vite + TypeScript)

### Init project
```bash
# at repo root
mkdir frontend && cd frontend
npm create vite@latest . -- --template react-ts
npm install
```

### Dev proxy to the backend
Edit `vite.config.ts`:
```ts
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '/api': 'http://localhost:8080', // forward /api to Spring Boot
    },
  },
})
```

### Architecture (Frontend)
- **Model**: Type definitions for `Supply` (or `MedicalSupply`).
- **View**: React components & pages (Home, Store, Admin, Search).  
  - `SuppliesTable` includes **Status** column: amount `> 10` (green), `5–10` (orange), `< 5` (red).
  - `SupplyForm` handles create/update; in edit mode, the `name` (key) is immutable.
- **Controller**: API client (`fetch` wrappers) used by pages/components.

---

## Final Project Structure (Reference)

```
├─ backend/                      # Spring Boot API
│  ├─ src/main/java/com/qare/app
│  │  ├─ controller/QareController.java
│  │  ├─ service/QareService.java
│  │  ├─ config/{DBConfig, CorsConfig}.java
│  │  └─ model/MedicalSupply.java
│  └─ src/main/resources/application.yml
├─ frontend/                     # React UI
│  ├─ src/
│  │  ├─ controller/supplies_client.ts
│  │  ├─ view/
│  │  │  └─ components/{SupplyForm, SupplyTable}.tsx
│  │  │  └─ pages/{Home,Store,Admin,Search}.tsx
│  │  ├─ model/supply_model.ts
│  │  └─ App.tsx
│  └─ vite.config.ts
└─ start.bash                    # simple dev starter (backend + frontend)
```

---

## Dev Tips

- **Start everything**:
  ```bash
  chmod +x start.bash
  ./start.bash
  ```
  Frontend → <http://localhost:5173> • Backend → <http://localhost:8080>

- **Auto-restart backend** (optional): add `spring-boot-devtools` and enable auto-make/compile in your IDE.

- **Switch DB to Postgres**: swap dependency + configure datasource; keep CRUD code unchanged.

- **Security** (future): add Spring Security; protect Admin routes; JWT for APIs or cookie auth with CSRF protection.

---

## Useful Endpoints (Backend)

- **Create** — `POST /api/supplies` → `201 Created` + `Location` header  
- **Read all** — `GET /api/supplies` → `MedicalSupply[]`  
- **Read one** — `GET /api/supplies/{name}` → `200` or `404`  
- **Update** — `PUT /api/supplies/{name}` → `200` or `404`  
- **Delete** — `DELETE /api/supplies/{name}` → `204` or `404`

---

## Q&A

- **Why is CORS not security?**  
  CORS only controls which **browsers** may read responses; it doesn’t prevent direct calls to your API nor authenticate users. Use authentication/authorization for real protection.

- **Why H2 for dev?**  
  Fast, simple, no install. You can still test DB constraints and swap to Postgres for staging/prod.

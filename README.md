# Qare — Medical Supplies Tracker

Qare is a simple full-stack app to track medical supplies. The backend exposes a clean REST API; the frontend provides a fast, friendly UI with pages for **Home**, **Store**, **Admin**, and **Search**.

- **Home:** quick intro with a hero image  
- **Store:** read-only table of all supplies  
- **Admin:** add, update, delete supplies  
- **Search:** find a supply by name  
- **Status colors:** amount `> 10` (green), `5–10` (orange), `< 5` (red)

---

## Tech stack

### Backend
- Java 21, Maven, Spring Boot 3  
- Spring Web, Validation, JDBC (H2 file DB by default)  
- JUnit 5 (Jupiter), Spring Test, Mockito

### Frontend
- React 18, TypeScript, Vite  
- React Router

---

## Project structure

```
.
├─ backend/                      # Spring Boot API
│  ├─ src/main/java/com/qare/app
│  │  ├─ controller/QareController.java
│  │  ├─ service/QareService.java
│  │  ├─ config/DBConfig.java
│  │  └─ model/MedicalSupply.java
│  └─ src/main/resources/application.yml
├─ frontend/                     # React UI
│  ├─ src/
│  │  ├─ controller/supplies_client.ts
│  │  ├─ view/
│  │  │  └─ components/{SupplyForm, SupplyTable}.tsx
│  │  ├─ pages/{Home,Store,Admin,Search}.tsx
│  │  ├─ model/supply_model.ts
│  │  └─ App.tsx
│  └─ vite.config.ts
└─ start.bash                    # simple dev starter (backend + frontend)
```

---

## Endpoints

### Create
- **POST** `/api/supplies`  
  **Body:** `MedicalSupply` JSON  
  **Returns:** `201 Created` + `Location` header

### Read all
- **GET** `/api/supplies` → `MedicalSupply[]`

### Read one
- **GET** `/api/supplies/{name}` → `200` + item or `404`

### Update (replace by name)
- **PUT** `/api/supplies/{name}` → `200` or `404` if not found

### Delete
- **DELETE** `/api/supplies/{name}` → `204` or `404`

---

## Prerequisites

- Java 21 (JDK)  
- Node.js 18+ and npm  
- Ports **8080** (backend) and **5173** (frontend) available

---

## Quick start (development)

### One command (recommended)

```bash
# from repo root
chmod +x start.bash
./start.bash
```

This starts:

- Backend (Spring Boot) at <http://localhost:8080>  
- Frontend (Vite) at <http://localhost:5173>

> ⚠️ Ensure ports **8080** and **5173** are free before starting.

### Manual (two terminals)

**Terminal 1 — backend**
```bash
cd backend
./mvnw spring-boot:run
```

**Terminal 2 — frontend**
```bash
cd frontend
npm install
npm run dev
```

Open <http://localhost:5173>.


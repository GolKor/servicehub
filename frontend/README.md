# ServiceHub — Local Services Marketplace

Μια εφαρμογή marketplace που συνδέει πελάτες με επαγγελματίες παροχής υπηρεσιών. Οι πελάτες δημιουργούν αιτήματα υπηρεσιών, οι επαγγελματίες υποβάλλουν προσφορές, και μετά την αποδοχή μιας προσφοράς δημιουργείται κράτηση που ολοκληρώνεται με αξιολόγηση.

## Τεχνολογίες

**Backend**
- Java 21, Spring Boot 4.1.1
- Spring Data JPA (Hibernate)
- Spring Security (session-based authentication)
- MySQL 8.0
- Maven
- Swagger / OpenAPI (springdoc-openapi 3.0.0)
- Docker & Docker Compose

**Frontend**
- React (Vite)
- React Router

## Δομή Project

```
servicehub/
├── backend/          # Spring Boot REST API
│   ├── src/
│   ├── pom.xml
│   ├── docker-compose.yml
│   ├── Dockerfile
│   └── .env           (τοπικό, δεν ανεβαίνει στο git)
├── frontend/          # React SPA
│   ├── src/
│   └── package.json
└── README.md
```

## Προαπαιτούμενα

- [Docker Desktop](https://www.docker.com/products/docker-desktop/) (για το backend + MySQL)
- [Node.js](https://nodejs.org/) v18+ και npm (για το frontend)

## Εκτέλεση του Backend

### 1. Δημιούργησε το `.env` αρχείο

Μέσα στο `backend/`, δημιούργησε ένα αρχείο `.env` με το εξής περιεχόμενο (προσάρμοσε τα credentials όπως θες):

```env
MYSQL_DB=servicehub_db
MYSQL_USER=admin
MYSQL_PASSWORD=<τον-κωδικό-σου>
SPRING_PROFILES_ACTIVE=docker
```

### 2. Εκκίνηση με Docker Compose

```bash
cd backend
docker compose up -d
```

Αυτό θα:
- Κατεβάσει και θα τρέξει ένα container MySQL 8.0
- Κάνει build το Spring Boot image και θα το τρέξει
- Δημιουργήσει αυτόματα το database schema (μέσω Hibernate `ddl-auto=update`)

Το backend θα είναι διαθέσιμο στο: **http://localhost:8080**

### 3. Επαλήθευση

```bash
docker ps
```

Πρέπει να δεις δύο containers (`servicehub_db`, `servicehub_app`) με status `Up`.

### Χρήσιμες εντολές

```bash
# Δες τα logs
docker compose logs app

# Σταμάτησε τα containers (κρατάει τα δεδομένα)
docker compose stop

# Ξανάτρεξέ τα
docker compose start

# Πλήρες rebuild (μετά από αλλαγές κώδικα ή στο pom.xml)
docker compose down
docker compose build --no-cache app
docker compose up -d
```

## Εκτέλεση του Frontend

```bash
cd frontend
npm install
npm run dev
```

Το frontend θα είναι διαθέσιμο στο: **http://localhost:5173**

> Το backend πρέπει να τρέχει (`docker compose up -d`) πριν ξεκινήσεις το frontend, αλλιώς τα API calls θα αποτυγχάνουν.

## API Documentation (Swagger)

Μόλις τρέχει το backend, η πλήρης τεκμηρίωση του REST API είναι διαθέσιμη στο:

**http://localhost:8080/swagger-ui/index.html**

## Βασική ροή χρήσης

1. Δημιούργησε λογαριασμό (`/register`) — ρόλος `CUSTOMER` ή `PROFESSIONAL`
2. Σύνδεση (`/login`)
3. **Ως Customer**: δημιούργησε ένα Service Request
4. **Ως Professional**: δημιούργησε professional profile, μετά υπέβαλε offer σε ένα ανοιχτό request
5. **Ως Customer**: αποδέξου ένα offer → δημιουργείται Booking
6. **Ως Professional**: ολοκλήρωσε το booking
7. **Ως Customer**: άφησε review

## Database

Η MySQL τρέχει σε Docker container στο port **3307** (host) → 3306 (container). Μπορείς να συνδεθείς με οποιοδήποτε MySQL client (π.χ. MySQL Workbench) στο:

- Host: `127.0.0.1`
- Port: `3307`
- Username/Password: όπως ορίστηκαν στο `.env`

## Authentication

Η εφαρμογή χρησιμοποιεί **session-based authentication** μέσω Spring Security (όχι JWT). Το frontend στέλνει `credentials: 'include'` σε κάθε request ώστε το session cookie να διατηρείται.
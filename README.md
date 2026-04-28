# 🔐 AuthX – Secure Authentication & Authorization System

AuthX is a full-stack ready backend system built using **Spring Boot** that provides secure authentication and role-based authorization using **JWT (JSON Web Tokens)**.

---

## 🚀 Features

* 🔐 User Registration & Login
* 🔑 JWT-based Authentication
* 🛡️ Role-Based Access Control (RBAC)
* 🔒 Password Encryption (BCrypt)
* 📦 Clean Architecture (DTO, Service, Repository)
* ⚠️ Global Exception Handling
* 📊 Admin Dashboard APIs (User Management)

---

## 🧰 Tech Stack

* **Backend:** Spring Boot, Spring Security
* **Database:** MySQL
* **Authentication:** JWT (jjwt)
* **ORM:** Spring Data JPA (Hibernate)
* **Build Tool:** Maven

---

## 📁 Project Structure

```
authx-backend/
│── config/        # Security & Password config
│── controller/    # REST APIs
│── dto/           # Request/Response DTOs
│── entity/        # Database Entities
│── exception/     # Custom Exceptions & Handler
│── repository/    # JPA Repositories
│── security/      # JWT Filter & Utility
│── service/       # Business Logic
```

---

## 🔐 Authentication Flow

1. User registers → Password is encrypted using BCrypt
2. User logs in → JWT token is generated
3. Token is sent in headers:

   ```
   Authorization: Bearer <token>
   ```
4. JWT Filter validates token for protected APIs

---

## 👥 Roles

* **USER** → Basic access
* **ADMIN** → Manage users (view, delete, update roles)

---

## 📡 API Endpoints

### 🔓 Public APIs

| Method | Endpoint         | Description       |
| ------ | ---------------- | ----------------- |
| POST   | `/auth/register` | Register new user |
| POST   | `/auth/login`    | Login & get JWT   |

---

### 🔐 User APIs

| Method | Endpoint        | Access       |
| ------ | --------------- | ------------ |
| GET    | `/user/profile` | USER / ADMIN |

---

### 👑 Admin APIs

| Method | Endpoint           | Access |
| ------ | ------------------ | ------ |
| GET    | `/admin/users`     | ADMIN  |
| DELETE | `/admin/user/{id}` | ADMIN  |
| PUT    | `/admin/user/role` | ADMIN  |

---

## ⚙️ Setup Instructions

### 1. Clone Repository

```
git clone https://github.com/anil-chakravarthi/authx-secure-auth-system.git
cd authx-secure-auth-system
```

---

### 2. Configure Database

Update your local configuration file:

```
application-dev.properties
```

Example:

```
spring.datasource.url=jdbc:mysql://localhost:3306/authx_db
spring.datasource.username=root
spring.datasource.password=your_password
```

---

### 3. Run Application

```
mvn spring-boot:run
```

---

## 🧪 Testing (Postman)

1. Login → Get token
2. Add header:

```
Authorization: Bearer <token>
```

3. Access protected APIs

---

## 📌 Future Enhancements

* Email verification
* Refresh tokens
* OAuth login (Google/GitHub)
* Frontend (React Dashboard)

---

## 👨‍💻 Author

**Anil Chakravarthi Meesala**
🔗 GitHub: https://github.com/anil-chakravarthi

---

## ⭐ If you like this project

Give it a ⭐ on GitHub!

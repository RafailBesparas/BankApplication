# 💳 BesparasBank – Enterprise-Ready Banking Web Application (Version 2)

**BesparasBank** is a secure, modular, and fully documented Spring Boot-based web banking system built for enterprise-grade usage, development training, and compliance demonstration.

---

## 🧭 Overview

This application provides users with full banking capabilities including:

- Registration and Login
- Secure Dashboard with Balance Overview
- Profile Management (PII-compliant)
- Deposits, Withdrawals, Transfers
- Transaction History
- Thymeleaf-based UI with modular fragments

---

## 🧱 Layered Architecture

```
org.example
├── controller
│   ├── BankController.java
│   ├── AccountSummaryController.java
│   └── ClientProfileController.java
│
├── service
│   ├── AccountService.java
│   └── ClientProfileService.java
│
├── repository
│   ├── AccountRepository.java
│   ├── ClientProfileRepository.java
│   └── TransactionRepository.java
│
├── model
│   ├── AccountModel.java
│   ├── ClientProfile.java
│   └── Transaction.java
│
├── config
│   └── SecurityConfig.java
│
└── BankingApplication.java
```

---

## 🧩 Key Features

### 🔒 Security

- Spring Security-based filter chain
- BCrypt password hashing
- Session-based authentication
- Controller-level protection and login/logout routes

### 📄 Profile & Account

- View/edit user profile (ClientProfile)
- Account linking and validation
- GDPR-friendly annotations and secure storage

### 💸 Transactions

- Deposit, Withdraw, Transfer with validation
- Bidirectional transaction logging (Transfer In/Out)
- Transaction history in dashboard and reports

---

## 🎨 UI Pages (Thymeleaf)

| Page | Purpose |
|------|---------|
| `/login` | Secure login |
| `/register` | New account creation |
| `/dashboard` | Account overview + operations |
| `/profile` | View profile |
| `/profile/edit` | Edit profile |
| `/account-summary` | Balance + recent transactions |
| `/transactions` | Full history view |

---

## 📜 Documentation & Compliance

Every class, method, and entity is documented with:

- **JavaDoc annotations**
- **Spring annotations**
- **GDPR/PII notes**
- **Security & Audit insights**
- **Domain-specific comments for banking**

All services follow **Separation of Concerns**, and repositories are designed to comply with **traceability, performance, and modularity**.

---

## ⚙️ Technologies Used

| Layer | Technology |
|-------|------------|
| Backend | Spring Boot |
| Security | Spring Security |
| ORM | Spring Data JPA |
| Frontend | Thymeleaf, Bootstrap |
| DB | H2/MySQL (configurable) |
| Java Version | 17+ |

---

## 🚀 Run Locally

```bash
git clone https://github.com/yourusername/besparasbank.git
cd besparasbank
./mvnw spring-boot:run
```

Open browser: [http://localhost:8080](http://localhost:8080)

---

## 👨‍💻 Developer

Documented, built, and reviewed for production by **Rafael Besparas**

---

## 📌 License

MIT License – For educational, demonstration, and enterprise prototyping use.


# 💳 BesparasBank – Spring Boot Banking Platform (v3)

Yes I have used Chatgtp to help me coding, no I do not understand all I am doing 100%. Yes I am exposing myself to more hardcore coding every day.

**BesparasBank** is an enterprise-inspired banking web application built with Java 17, Spring Boot, and Thymeleaf. Version 3 introduces an even cleaner architecture, enhanced UI, improved transaction filtering, and future support for financial analytics and dashboard features.

---

## 🚀 Features at a Glance

- 🔐 Secure user authentication (login/register)
- 🏦 Core banking actions: deposit, withdraw, transfer
- 📜 Filterable transaction history (type, date, range)
- 👤 Editable client profiles (PII/GDPR-friendly)
- 🔔 Notification system for key account events
- 📊 Dashboard with last recipient + balance summary
- 💻 Responsive modern UI (Thymeleaf + CSS)
- ☕ Java 17 + Spring Boot layered architecture

---

## 🏗️ Layered Architecture

```
org.example
├── controller          // Web & REST endpoints
├── service             // Business logic
├── repository          // Data access via Spring Data JPA
├── model               // Entity and domain classes
├── config              // Security configuration
└── BankingApplication  // Main entry point
```

---

## 🔑 Security Highlights

- 🔒 Passwords secured with BCrypt
- 🧩 Spring Security session-based auth
- 🔐 Controller-level endpoint protection
- ✅ CSRF and form validation support

---

## 💼 Functional Modules

| Module         | Description                                       |
|----------------|---------------------------------------------------|
| Account        | Balance handling, transfers, and dashboard logic  |
| Transactions   | Full searchable history with filters              |
| Profile        | View/edit PII-compliant user info                 |
| Notifications  | Event-triggered alerts (low balance, transfers)   |
| Loans (WIP)    | Application, approval, and repayment plans        |

---

## 🖥️ UI Pages

| URL               | Description                            |
|-------------------|----------------------------------------|
| `/login`          | User authentication                    |
| `/register`       | Account creation                       |
| `/dashboard`      | Account overview, quick actions        |
| `/transactions`   | History with filters                   |
| `/account-summary`| High-level balance & transaction view  |
| `/profile`        | View and edit user profile             |

---

## ⚙️ Technologies Used

| Layer     | Stack                             |
|-----------|-----------------------------------|
| Backend   | Java 17, Spring Boot 3.x          |
| Security  | Spring Security                   |
| ORM       | Spring Data JPA (Hibernate)       |
| Frontend  | Thymeleaf + Bootstrap CSS         |
| Database  | H2 (dev) / MySQL (prod-ready)     |
| Messaging | Apache Kafka (optional module)    |

---

## 📦 Project Setup (Local)

```bash
git clone https://github.com/yourusername/besparasbank.git
cd besparasbank
./mvnw spring-boot:run
```

Access in browser: [http://localhost:8080](http://localhost:8080)

---

## 📁 Sample Data & Testing

- Preload sample transactions with SQL seed (optional)
- Add test accounts via `/register`
- Filter by type/date/amount via `/transactions`

---

## 🧠 Next in Version 4 (Planned)

- Dashboard charts (Chart.js)
- Account analytics & spending insights
- Admin dashboard and approval workflows
- Docker support with `docker-compose`

---

## 👨‍💻 Author

Built with care and precision by **Rafael Besparas**  
📧 [your.email@example.com](mailto:your.email@example.com)

---

## 📄 License

MIT License – For learning, demonstration, and prototyping use.

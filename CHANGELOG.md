# 📘 Changelog – BesparasBank

## v3 – May 2025

### 🆕 New Features

- **💳 Filterable Transaction History**
  - Filter by date range, amount (min/max), and transaction type
  - Fully integrated into the Thymeleaf UI

- **🧾 Enhanced Table UI**
  - Modernized HTML & CSS for transaction tables
  - Responsive layout for smaller screens
  - Color-coded values (e.g., green for deposits)

- **🔧 Modular CSS & JS**
  - Extracted all styling into `transactions.css`
  - Filter functionality handled via `transactions.js`

- **📁 Readme Overhaul**
  - Fully rewritten `README.md` with feature list, tech stack, and layered architecture
  - Added professional formatting for GitHub presentation

---

### 🧹 Removed

- ❌ GPT/LLM "Explain Transaction" feature
  - Removed Python server integration and related Spring controller
  - Deleted modal, buttons, and JS logic associated with LLM

---

### 🔄 Improvements

- ✅ Simplified Thymeleaf HTML templates
- ✅ Clean separation of concerns in frontend assets
- ✅ Version-controlled folder structure and module naming
- ✅ Ready-to-use for job applications, technical interviews, or project demos

---

## 📌 Planned for Version 4

- 📊 Dashboard visualizations with Chart.js
- 🧠 Spending insights and financial tips engine
- 🛠️ Admin module with loan approval workflows
- 🐳 Docker Compose support
- 🧪 Unit and integration test coverage

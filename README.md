# VoteEase - Spring Boot Voting System

VoteEase is a complete online voting system built with **Spring Boot** and **Thymeleaf**, featuring integrated server-side rendering with robust security and real-time functionality.

## 🚀 Features

- **User Authentication** (Session-based security)
- **Role-based Access Control** (Candidate, Voter, Admin, Election guider)
- **Secure Voting System** (Prevent duplicate votes)
- **Real-time Vote Counting** (Thymeleaf live updates)
- **Audit Logs & Data Integrity**
- **Template-driven UI** (Server-side rendered views)

## 🏗️ Tech Stack

### **Server Components**

- **Spring Boot** - Core application framework
- **Thymeleaf** - Server-side templating engine
- **Spring Security** - Authentication & authorization
- **Spring Data JPA (Hibernate)** - Database ORM layer
- **PostgreSQL** - Persistent data storage
- **Lombok** - Boilerplate reduction

### UI Layer

- **Bootstrap 5** - Responsive frontend components
- **Thymeleaf Fragments** - Reusable UI templates
- **Spring MVC** - Controller-driven views
- **Form Binding** - Thymeleaf + Spring validation

### Prerequisites

- Java 17+
- Maven
- PostgreSQL

## Resources used

- [Spring Security](https://www.youtube.com/watch?v=X7pGCmVxx10&t=689s)

## 📸 Application Workflow (Screenshots)

### 🏠 Main Page

> Homepage with navigation and basic description  
> ![Main Page](./images/home1.png)

---

### 🧑‍💼 Election Creator Flow

1. **Login Page**  
   ![Login](./images/guider/1login.png)

2. **Register Page**  
   ![Register](./images/guider/2register.png)

3. **Dashboard (Created Elections)**  
   ![Dashboard](./images/guider/3created_elections1.png)

4. **Create New Election (Step 1)**  
   ![New Election 1](./images/guider/4new_election1.png)

5. **Create New Election (Step 2)**  
   ![New Election 2](./images/guider/5new_election2.png)

6. **Add Candidates (Step 1)**  
   ![Candidates 1](./images/guider/6candidates1.png)

7. **Add Candidates (Step 2)**  
   ![Candidates 2](./images/guider/7candidates2.png)

8. **Start Election (Get Code)**  
   ![Start Code](./images/guider/8starting_election_code.png)

9. **End Election**  
   ![End Election](./images/guider/9ending_election.png)

10. **View Election Results**  
    ![Results](./images/guider/10election_results.png)

---

### 🗳️ Election Voter Flow

1. **Voter Dashboard**  
   ![Voter Dashboard](./images/voter/1voter_dashb.png)

2. **Get Voting Code**  
   ![Voting Code](./images/voter/2getting_votting_code.png)

3. **Voting Page**  
   ![Voting Page](./images/voter/3voting_page.png)

4. **Vote Confirmation**  
   ![Vote OK](./images/voter/4voting_ok.png)

5. **Request Results**  
   ![Get Results](./images/voter/5election_res_init.png)

6. **Election Results Page**  
   ![Results](./images/voter/6election_results.png)

---

## 📜 License

MIT License - Feel free to contribute and make Pull Request!

## ✉️ Contact

For any issues, reach out via **[GitHub Issues](https://github.com/Mchiir/voteEase/issues)** or Email **mugishachrispin590@gmail.com**.

---

🚀 **Happy Coding & Secure Voting!** 🗳️

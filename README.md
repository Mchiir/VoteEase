# VoteEase - Online Voting System

**VoteEase** is a full-featured online voting platform built with **Spring Boot**, **Thymeleaf**, and **PostgreSQL**. It supports secure, role-based access control and real-time vote tracking through server-side rendered views.

---

## Features

- User Authentication – Secure session-based login system
- Role-Based Access Control – Voter, Candidate, Admin, and Election Guider roles
- One Vote Per Voter Enforcement – Strict vote validation logic
- Live Results Rendering – Real-time vote counting via SSR
- Audit Logging – Persistent records for transparency
- Templated UI – Modular Thymeleaf-based layout and dynamic rendering

---

## Architecture and Tech Stack

### Backend

- Spring Boot – Core application framework
- Spring MVC – REST + server-side view handling
- Spring Security – Role-based authentication and authorization
- Spring Data JPA (Hibernate) – ORM for PostgreSQL
- Lombok – Reduces boilerplate in Java classes

### Frontend (SSR)

- Thymeleaf – Template engine for rendering dynamic views
- Bootstrap 5 – Responsive layout and design
- Thymeleaf Fragments – For modular templates
- Spring Validation – Form validation using annotations

---

### Prerequisites

- Java 17+
- Maven
- PostgreSQL

## Resources used

- [Spring Security - YouTube Guide](https://www.youtube.com/watch?v=X7pGCmVxx10&t=689s)
- [Connecting to Neon PostgreSQL](https://neon.tech/docs/guides/java)
- [Hiding Credentials in Spring Boot](https://hackernoon.com/how-to-hide-credentials-in-spring-boot)
- [Docker with Spring Boot & PostgreSQL](https://medium.com/@saygiligozde/using-docker-compose-with-spring-boot-and-postgresql-235031106f9f)
- [Dockerize Spring Boot Project Demo](https://github.com/Mchiir/Java/blob/testdocker-java)

## Application Workflow (Screenshots)

### Main Page

> Homepage with navigation and basic description  
> ![Main Page](./images/home1.png)

---

### Election Creator Flow

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

### Election Voter Flow

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

### Help Wanted: Improve Role-Based Security

> **Spring Security Role Handling Needs Enhancement**

While the app has basic Spring Security in place, **role-based access control is not fully implemented**. User roles specifically `ADMIN`, and `GUIDER` are required and must be enforced properly across the system.

**Tasks:**

- Enforce access restrictions based on user roles
- Ensure protected views/controllers validate roles correctly
- Add default role assignment logic on user registration
- Optionally add logic for role switching (e.g., `ADMIN` managing users)

**How to contribute:**  
Fork the repo, work on a new branch like `feature/security-roles`, and submit a pull request when done.

---

## Contributing

Want to improve VoteEase? Here's how:

1. **Fork** the repository
2. **Create a new feature branch**
   ```bash
   git checkout -b feature/YourFeature
   ```
3. **Commit your changes**
   ```bash
   git commit -m "Add your feature"
   ```
4. **Push to your branch**
   ```bash
   git push origin feature/YourFeature"
   ```
5. **Open a pull request on GitHub**

> **Note:** If you're contributing to Spring Security roles, please refer to the  
> [Help Wanted: Improve Role-Based Security](#help-wanted-improve-role-based-security) section above for specific guidance.

---

## Resources

- [Docker containerization](https://www.geeksforgeeks.org/springboot/how-to-dockerize-a-spring-boot-application-with-maven)

## License

MIT License - Feel free to contribute and make Pull Request!

## Contact

For any issues, reach out via **[GitHub Issues](https://github.com/Mchiir/voteEase/issues)** or Email **mugishachrispin590@gmail.com**.

---

**Happy Coding & Secure Voting!**

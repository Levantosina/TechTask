# TechTask

A microservice for managing users and their subscriptions to digital services (YouTube Premium, Netflix, etc.).

## 🚀 Technology Stack

- Java 17
- Spring Boot 3
- Spring Data JPA
- Liquibase
- PostgreSQL
- Docker, Docker Compose
- SLF4J (logging)
- Maven

### **Project Setup**
- `Cloning the project:` 
```bash
  git clone https://github.com/Levantosina/TechTask.git
  ```
- `Navigating to the project directory:`
```bash
  cd techTask
   ```
- `Starting containers in the background:`
```bash
  docker compose up -d
 ```
- `Viewing logs of the subscription service:`
```bash
  docker logs -f subscription
 ```
- `Stopping and removing containers:`
```bash
  docker compose down
 ```

### 👤 User Management
- `POST /users` — create a user
- `GET /users/{id}` — get a user
- `PUT /users/{id}` — update user data
- `DELETE /users/{id}` — delete a user

### 🕹️ Subscription Management
- `POST /users/{id}/subscriptions` — add a subscription to a user
- `GET /users/{id}/subscriptions` — get user subscriptions
- `DELETE /users/{id}/subscriptions/{sub_id}` — delete a subscription
- `GET /subscriptions/top` — get the TOP-3 popular subscriptions

### 🛠  Request Examples
- `Creating a new user`
```
POST /users
Content-Type: application/json
{
    "firstName":"FirstName",
    "lastName":"LastName",
    "email":"testEmail@gmail.com"
}
```
- `Creating a new subscription`
```
POST /users/1/subscriptions
Content-Type: application/json
{
  "subscriptionName": "Google",
  "monthlyPrice": 15.99,
  "endDate": "2025-12-31"
}
```
 🙋Developed as part of a test task.  
 💭For inquiries: levantosina1992@gmail.com


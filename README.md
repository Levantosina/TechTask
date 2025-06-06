# TechTask


Микросервис для управления пользователями и их подписками на цифровые сервисы (YouTube Premium, Netflix др.).

## 🚀 Стек технологий

- Java 17
- Spring Boot 3
- Spring Data JPA
- Liquibase
- PostgreSQL
- Docker, Docker Compose
- SLF4J (логирование)
- Maven

### **Настройка проекта**
- `Клонирование проекта:` 
```bash
  git clone https://github.com/Levantosina/TechTask.git
  ```
- `Переход в директорию проекта:`
```bash
  cd techTask
   ```
- `Запуск контейнеров в фоновом режиме:`
```bash
  docker compose up -d
 ```
- `Просмотр логов сервиса subscription`
```bash
  docker logs -f subscription
 ```
- `Остановка и удаление контейнеров:`
```bash
  docker compose down
 ```

### 👤 Управление пользователями
- `POST /users` — создать пользователя
- `GET /users/{id}` — получить пользователя
- `PUT /users/{id}` — обновить данные пользователя
- `DELETE /users/{id}` — удалить пользователя

### 🕹️ Управление подписками
- `POST /users/{id}/subscriptions` — добавить подписку пользователю
- `GET /users/{id}/subscriptions` — получить подписки пользователя
- `DELETE /users/{id}/subscriptions/{sub_id}` — удалить подписку
- `GET /subscriptions/top` — получить ТОП-3 популярных подписок

### 🛠  Примеры запросов
- `Создание нового юзера`
```
POST /users
Content-Type: application/json
{
    "firstName":"FirstName",
    "lastName":"LastName",
    "email":"testEmail@gmail.com"
}
```
- `Создание новой подписки`
```
POST /users/1/subscriptions
Content-Type: application/json
{
  "subscriptionName": "Google",
  "monthlyPrice": 15.99,
  "endDate": "2025-12-31"
}
```
 🙋Разработано в рамках тестового задания.  
 💭По вопросам: `levantosina1992@gmail.com`


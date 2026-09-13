# Spring Task API

A simple REST API for managing tasks, built with Spring Boot.

This project was created as part of my Java backend development practice.

## Technologies

- Java 21
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Hibernate
- PostgreSQL
- Gradle
- Jakarta Validation

## Features

- Create a task
- Get all tasks
- Get a task by ID
- Update a task
- Delete a task
- Request validation
- HTTP error handling
- PostgreSQL persistence

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/tasks` | Create a task |
| GET | `/tasks` | Get all tasks |
| GET | `/tasks/{id}` | Get task by ID |
| PUT | `/tasks/{id}` | Update task |
| DELETE | `/tasks/{id}` | Delete task |

## Example Request

POST `/tasks`

```json
{
  "title": "Learn Spring Boot",
  "completed": false
}
```

## Configuration

The application uses PostgreSQL.

Create a PostgreSQL database and configure the connection in
`src/main/resources/application.properties`.

The database password is provided through the `DB_PASSWORD`
environment variable:

```bash
export DB_PASSWORD=your_password
```
Example configuration:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/backend_db
spring.datasource.username=backend_user
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

## Run the Application

Make sure PostgreSQL is running:

```bash
sudo systemctl start postgresql
```

Set the database password:

```bash
export DB_PASSWORD=your_password
```

Then start the Spring Boot application:

```bash
./gradlew bootRun
```

The application runs by default on:

`http://localhost:8080`

## Example Usage

Create a task:

```http
POST /tasks
```

Request body:

```json
{
  "title": "Learn Spring Boot",
  "completed": false
}
```

Example response:

```json
{
  "id": 1,
  "title": "Learn Spring Boot",
  "completed": false
}
```

Get all tasks:

```http
GET /tasks
```

Get a task by ID:

```http
GET /tasks/1
```

Update a task:

```http
PUT /tasks/1
```

Request body:

```json
{
  "title": "Learn Spring Boot and JPA",
  "completed": true
}
```

Delete a task:

```http
DELETE /tasks/1
```

A successful deletion returns:

```text
204 No Content
```

If a requested task does not exist, the API returns:

```text
404 Not Found
```

If validation fails, for example when the title is blank, the API returns:

```text
400 Bad Request
```
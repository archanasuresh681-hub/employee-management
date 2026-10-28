# Employee Management REST API

## Project Overview

A RESTful backend application developed using Java and Spring Boot to manage employee records. The application supports employee CRUD operations, search, filtering, pagination, sorting, and request validation.

## Technologies Used

- Java 21
- Spring Boot
- Spring Data JPA
- Hibernate
- PostgreSQL
- Maven
- Lombok
- Swagger / OpenAPI
- JUnit and Mockito

## Features

- Create, retrieve, update, and delete employees
- Retrieve an employee by ID
- Search employees
- Filter employees by status
- Pagination and sorting
- Request validation and exception handling

## Prerequisites

- JDK 21
- PostgreSQL
- Maven (or Maven Wrapper)

## Database Configuration

Create a PostgreSQL database:

```sql
CREATE DATABASE employee_management;
```

Configure the database connection in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/employee_management
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD}
```

## How to Run

1. Clone the repository:

   ```bash
   git clone <YOUR_GITHUB_REPOSITORY_URL>
   ```

2. Navigate to the project directory:

   ```bash
   cd employee-management
   ```

3. Run the application on Windows:

   ```bat
   mvnw.cmd spring-boot:run
   ```

   Alternatively, run `mvn spring-boot:run` if Maven is installed.

4. The application runs at `http://localhost:8080`, unless configured otherwise.

## API Endpoints

Base URL: `http://localhost:8080/api/employees`

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/employees` | Create an employee |
| GET | `/api/employees` | Get all employees |
| GET | `/api/employees/{id}` | Get employee by ID |
| PUT | `/api/employees/{id}` | Update an employee |
| DELETE | `/api/employees/{id}` | Delete an employee |

The employee listing endpoint supports search, status filtering, pagination, and sorting when the corresponding parameters are supplied.

## Sample Request

**POST** `/api/employees`

```json
{
  "firstName": "Ishani",
  "lastName": "Krishna",
  "email": "ish@example.com",
  "phone": "9876543218",
  "department": "IT",
  "designation": "Devops",
  "salary": 35000.00,
  "status": "ACTIVE"
}
```

## Sample Response

```json
{
    "success": true,
    "message": "Employee created successfully",
    "data": {
        "id": 9,
        "firstName": "Ishani",
        "lastName": "Krishna",
        "email": "ish@example.com",
        "phone": "9876543218",
        "department": "IT",
        "designation": "Devops",
        "salary": 35000.00,
        "status": "ACTIVE",
        "createdAt": "2026-10-28T15:37:48.0408954",
        "updatedAt": "2026-10-28T15:37:48.0408954"
    }
}
```

## API Documentation

If Swagger is configured, open the following URL after starting the application:

`http://localhost:8080/swagger-ui.html`

## Running Tests

Run the test suite using:

```bat
mvnw.cmd test
```

## Author

Developed as a Java Spring Boot Employee Management REST API project.

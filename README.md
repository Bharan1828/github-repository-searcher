# GitHub Repository Searcher

A Spring Boot application that integrates with the GitHub REST API to search repositories, store repository information in PostgreSQL, and retrieve saved repositories with filtering and sorting options.

## Features

- Search GitHub repositories using repository names or keywords.
- Filter search results by programming language.
- Sort search results by stars, forks, or last updated date.
- Store repository information in PostgreSQL.
- Update existing repository records instead of creating duplicates.
- Retrieve stored repositories using optional filters.
- Handle invalid requests and GitHub API errors.
- Validate incoming search requests.
- RESTful API architecture.

## Technologies Used

| Technology | Purpose |
|---|---|
| Java 21 | Programming language |
| Spring Boot | Backend framework |
| Spring Web | REST API development |
| Spring Data JPA | Database operations |
| Hibernate | ORM |
| PostgreSQL | Relational database |
| Maven | Dependency management |
| Git & GitHub | Version control |
| Postman | API testing |
| JUnit 5 | Unit testing |
| Mockito | Mocking dependencies |
| Swagger / OpenAPI | API documentation |

## Project Structure

```text
src
├── main
│   ├── java
│   │   └── com.example.githubsearcher
│   │       ├── config
│   │       │   └── AppConfig.java
│   │       ├── controller
│   │       │   └── GithubController.java
│   │       ├── dto
│   │       │   ├── GithubSearchRequest.java
│   │       │   ├── GithubSearchResponse.java
│   │       │   └── GithubRepositoriesResponse.java
│   │       ├── exception
│   │       │   └── GlobalExceptionHandler.java
│   │       ├── model
│   │       │   └── GithubRepository.java
│   │       ├── Repository
│   │       │   └── GithubRepositoryRepository.java
│   │       ├── service
│   │       │   └── GithubService.java
│   │       └── GithubRepositorySearcherApplication.java
│   └── resources
│       └── application.properties
└── test
    └── java
        └── com.example.githubsearcher
            └── service
                └── GithubServiceTest.java
```

## Database Configuration

This project uses PostgreSQL to store GitHub repository information.

Create a database named:

```sql
CREATE DATABASE github_repo_db;
```

Configure the database connection in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/github_repo_db
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

Replace `YOUR_PASSWORD` with your PostgreSQL password.

## Installation and Setup

### Prerequisites

- Java 21
- Maven
- PostgreSQL
- Git
- Postman (for API testing)

### Steps

**1. Clone the repository**

```bash
git clone YOUR_GITHUB_REPOSITORY_URL
```

**2. Navigate to the project directory**

```bash
cd GithubRepositorySearcher
```

**3. Configure PostgreSQL**

Create the database and update your database credentials in `application.properties`.

**4. Run the application**

Using Maven:

```bash
mvn spring-boot:run
```

Or run `GithubRepositorySearcherApplication.java` from your IDE.

The application starts at:

```text
http://localhost:8080
```

## API Documentation

### 1. Search GitHub Repositories

**Endpoint**

```http
POST /api/github/search
```

**Request Body**

```json
{
  "query": "spring boot",
  "language": "Java",
  "sort": "stars"
}
```

**Supported sort values**

- `stars`
- `forks`
- `updated`

**Example Response**

```json
{
  "message": "Repositories fetched and saved successfully",
  "repositories": [
    {
      "id": 123,
      "name": "spring-boot",
      "description": "Spring Boot Framework",
      "owner": "spring-projects",
      "language": "Java",
      "stars": 50000,
      "forks": 30000,
      "lastUpdated": "2026-11-18T10:00:00Z"
    }
  ]
}
```

The response above illustrates the response structure.

### 2. Retrieve Stored Repositories

**Endpoint**

```http
GET /api/github/repositories
```

Returns repositories stored in the PostgreSQL database.

**Optional Query Parameters**

| Parameter | Description |
|---|---|
| language | Filter by programming language |
| minStars | Minimum number of stars |
| sort | Sort by stars, forks, or updated |

**Examples**

Retrieve all repositories:

```http
GET http://localhost:8080/api/github/repositories
```

Filter by language:

```http
GET http://localhost:8080/api/github/repositories?language=Java
```

Filter by minimum stars:

```http
GET http://localhost:8080/api/github/repositories?minStars=10000
```

Sort by forks:

```http
GET http://localhost:8080/api/github/repositories?sort=forks
```

Sort by last updated date:

```http
GET http://localhost:8080/api/github/repositories?sort=updated
```

Combine filters:

```http
GET http://localhost:8080/api/github/repositories?language=Java&minStars=10000&sort=forks
```

## Error Handling

The application handles:

- Empty or invalid search queries.
- Invalid sort values.
- GitHub API errors.
- GitHub API connection failures.
- Invalid responses from GitHub.

Appropriate HTTP status codes are returned for handled errors.

## Running Unit Tests

The project uses JUnit 5 and Mockito for unit testing.

Run all tests using:

```bash
mvn test
```

The tests cover:

- Repository filtering and sorting by forks.
- Default sorting by stars.
- Sorting by last updated date.
- Fetching and saving repositories using mocked dependencies.
- Handling invalid GitHub API responses.

## API Testing

The REST endpoints can be tested using Postman.

Swagger UI is also available at:

```text
http://localhost:8080/swagger-ui.html
```

## License

This project was developed as a backend application assignment.
# Java Spring Boot REST API

Just for my learning purposes.

## Overview

This project is a RESTful API built using Java Spring Boot, designed to manage contacts and their addresses. It includes user authentication, session management, and a PostgreSQL database.

## Features

- **User Authentication**: Secure login with JWT token-based authentication
- **Redis Session Storage**: Fast token validation and session management
- **PostgreSQL Database**: Persistent data storage with proper relationships
- **RESTful API Design**: Standard HTTP methods and status codes
- **Contact Management**: CRUD operations for contacts
- **Address Management**: CRUD operations for contact addresses

## Technology Stack

- **Java 24**: Core programming language
- **Spring Boot**: Application framework
- **Spring Data JPA**: Database access and ORM
- **PostgreSQL**: Relational database
- **Redis**: In-memory data store for session management
- **JWT**: JSON Web Tokens for authentication
- **Maven**: Dependency management and build tool
- **Lombok**: Reduces boilerplate code
- **HikariCP**: High-performance JDBC connection pool

## Project Structure

├── src/main/java/javabasicapi/restful/
│ ├── config/ # Configuration classes
│ ├── controller/ # REST API endpoints
│ ├── dto/ # Data Transfer Objects
│ ├── entity/ # JPA entity classes
│ ├── middleware/ # Request interceptors and filters
│ ├── pkg/ # Utility packages
│ ├── repository/ # Data access interfaces
│ ├── security/ # Security-related classes
│ └── service/ # Business logic
├── src/main/resources/
│ └── application.properties # Application configuration
├── migrations/ # SQL migration scripts
└── pom.xml # Maven dependencies

## Authentication Flow

1. User submits credentials to `/api/login`
2. System validates credentials against database
3. If valid, a JWT token is generated with user ID and expiration
4. Token is stored in Redis with user ID as value
5. Token is also saved to user entity in database
6. Token is returned to client
7. For subsequent requests, client includes token in `X-API-TOKEN` header
8. System validates token by:
   - Verifying JWT signature and expiration
   - Checking Redis for token existence (fast path)
   - Falling back to database check if not in Redis

## Database Schema

### Users Table

```sql
CREATE TABLE users (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    username VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    token VARCHAR(100),
    token_expired_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT users_username_unique UNIQUE (username),
    CONSTRAINT users_token_unique UNIQUE (token)
);
```

### Contacts Table

```sql
CREATE TABLE contacts (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    user_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT contacts_user_id_fk FOREIGN KEY (user_id) REFERENCES users(id)
);
```

### Addresses Table

```sql
CREATE TABLE addresses (
    id UUID PRIMARY KEY,
    street VARCHAR(100) NOT NULL,
    city VARCHAR(100) NOT NULL,
    province VARCHAR(100) NOT NULL,
    postal_code VARCHAR(100) NOT NULL,
    country VARCHAR(100) NOT NULL,
    contact_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT addresses_contact_id_fk FOREIGN KEY (contact_id) REFERENCES contacts(id)
);
```

## Setup and Installation

### Prerequisites

- Java 24
- Maven
- PostgreSQL
- Redis

### Configuration

1. Clone the repository
2. Configure database connection in src/main/resources/application.properties :

```
spring.datasource.url=jdbc:postgresql://localhost:5432/your_db_name
spring.datasource.username=your_username
spring.datasource.password=your_password
```

3. Configure Redis connection in src/main/resources/application.properties :

```
spring.redis.host=localhost
spring.redis.port=6379
```

4. Configure JWT secret in src/main/resources/application.properties :

```
jwt.secret=your_jwt_secret_key
jwt.expiration=1296000
```

### Database Migration

Run database migration scripts located in migrations directory.

```
make migrate-up database="postgres://username:password@host:port/db_name?sslmode=disable"
```

### Running the Application

```
mvn spring-boot:run
```

or

```
make run
```

## API Endpoints

You can use postman collection in `postman/` folder and see the documentation.

### Authentication

- POST /api/login: Authenticate user and return JWT token
- POST /api/logout: Logout user and invalidate JWT token

### Users

- POST /api/register: Register a new user
- GET /api/users/me: Get profile
- PATCH /api/users/me: Update a user

### Contacts

- GET /api/contacts: Get all contacts
- GET /api/contacts/{contactId}: Get a specific contact
- POST /api/contacts: Create a new contact
- PUT /api/contacts/{contactId}: Update a contact
- DELETE /api/contacts/{contactId}: Delete a contact

### Addresses

- GET /api/addresses/{contactId}: Get all addresses for a contact
- GET /api/addresses/{contactId}/{addressId}: Get a specific address for a contact
- POST /api/addresses: Create a new address for a contact
- PUT /api/addresses/{contactId}/{addressId}: Update an address for a contact
- DELETE /api/addresses/{contactId}/{addressId}: Delete an address for a contact

## Security

- JWT token-based authentication
- Redis session management
- Password hashing using BCrypt

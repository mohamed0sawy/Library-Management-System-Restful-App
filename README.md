![Language](https://img.shields.io/badge/language-Java%20-blue.svg)
![Technologies](https://img.shields.io/badge/technologies-Spring_boot%20-green.svg)

# Library Management System 📖
# Overview
This project aims to develop a Restful API using the Spring Boot framework to handle HTTP requests and responses for a Library Management System. 
The system allows users to manage information about authors, books, customers (users), and borrowing records. 
It provides endpoints to perform CRUD (Create, Read, Update, Delete) operations on each entity, facilitating efficient management of library resources.

## The entities in the system include:
- **Author**: Contains information about authors of the books, including their name, birth date, and nationality.
- **Book**: Stores details of the books available in the library, such as title, ISBN, publication date, genre, and availability status.
- **Customer**: Manages information about library users, including their name, email, address, phone number, and encrypted password.
- **Borrowing Record**: Records books borrowed by users, including the borrowing date, return date, and references to the user and book.

# Technologies Used
- Spring Boot.
- Spring Data.
- MySQL for database.
- Swagger.

# Endpoints
1- **Authors:**
- `GET /authors`: Retrieve all authors.
- `GET /authors/{id}`: Retrieve an author by ID.
- `POST /authors`: Create a new author.
- `PUT /authors/{id}`: Update an existing author.
- `DELETE /authors/{id}`: Delete an author by ID.
  
2- **Books:**
- `GET /books`: Retrieve all books.
- `GET /books/{id}`: Retrieve a book by ID.
- `POST /books`: Create a new book.
- `PUT /books/{id}`: Update an existing book.
- `DELETE /books/{id}`: Delete a book by ID.
- `GET /books/search?title={title}`: Search for books by title.
- `GET /books/search?author={author}`: Search for books by author.
- `GET /books/search?isbn={isbn}`: Search for books by ISBN.
  
3- **Customers:**
- `GET /customers`: Retrieve all customers/users.
- `GET /customers/{id}`: Retrieve a customer/user by ID.
- `POST /customers`: Create a new customer/user.
- `PUT /customers/{id}`: Update an existing customer/user.
- `DELETE /customers/{id}`: Delete a customer/user by ID.
  
4- **Borrowing Records:**
- `GET /borrowings`: Retrieve all borrowing records.
- `GET /borrowings/{id}`: Retrieve a borrowing record by ID.
- `POST /borrowings`: Create a new borrowing record.
- `PUT /borrowings/{id}`: Update an existing borrowing record.
- `DELETE /borrowings/{id}`: Delete a borrowing record by ID.
- `GET /borrowings/search?userId={userId}`: Retrieve borrowing records for a specific user.
- `GET /borrowings/search?bookId={bookId}`: Retrieve borrowing records for a specific book.

# Features
The Library Management System offers several features to enhance usability, performance, and reliability:

1- **Pagination and Sorting:** 
- All endpoints supporting multiple records return data in paginated format, allowing clients to retrieve large datasets efficiently. Additionally, clients can specify sorting criteria to order the results based on different fields.

2- **Validation:** 
- The system enforces data integrity by performing validation checks on incoming requests. This ensures that only valid and properly formatted data is accepted, preventing inconsistencies and errors in the database.

3- **Global Exception Handling:** 
- Robust exception handling mechanisms are in place to gracefully manage errors and exceptions that may occur during request processing. This ensures that clients receive informative error responses, enhancing the overall reliability of the system.

4- **In-Memory Caching:** 
- To improve performance and reduce database load, the application utilizes in-memory caching techniques. This optimizes data retrieval operations by storing frequently accessed data in memory, resulting in faster response times and improved scalability.

package com.sawy.LibrarySystem.controller;

import com.sawy.LibrarySystem.model.Book;
import com.sawy.LibrarySystem.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/books")
    public ResponseEntity<Page<Book>> getAllBooks(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size,
                                                  @RequestParam(defaultValue = "id") String sortBy,
                                                  @RequestParam(defaultValue = "asc") String sortOrder) {
        Page<Book> books = bookService.getAllBooks(page, size, sortBy, sortOrder);
        books.getContent().forEach(book -> {
            book.setAuthorID(book.getAuthor().getId());
            book.getBorrowingRecords().forEach(record -> {
                record.setCustomerID(record.getCustomer().getId());
                record.setBookID(record.getBook().getId());
            });
        });
        return ResponseEntity.ok(books);
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Optional<Book> book = bookService.getBookById(id);
        return book.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/books")
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        Book createdBook = bookService.createBook(book);
        return ResponseEntity.ok(createdBook);
    }

    @PutMapping("/books/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book bookDetails) {
        Optional<Book> updatedBook = bookService.updateBook(id, bookDetails);
        return updatedBook.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/books/search")
    public ResponseEntity<Page<Book>> searchBooks(@RequestParam(required = false) String title,
                                                  @RequestParam(required = false) String author,
                                                  @RequestParam(required = false) String isbn,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size,
                                                  @RequestParam(defaultValue = "id") String sortBy,
                                                  @RequestParam(defaultValue = "asc") String sortOrder) {
        if ((title == null || title.isEmpty()) && (author == null || author.isEmpty()) && (isbn == null || isbn.isEmpty())) {
            // no parameters provided, return all books
            return ResponseEntity.ok(bookService.getAllBooks(page, size, sortBy, sortOrder));
        }

        // Search based on the provided parameters, the first one will be processed first and ignore what following
        Page<Book> books;
        if (title != null && !title.isEmpty()) {
            books = bookService.searchBooksByTitle(title, page, size, sortBy, sortOrder);
        } else if (author != null && !author.isEmpty()) {
            books = bookService.searchBooksByAuthor(author, page, size, sortBy, sortOrder);
        } else {
            books = bookService.searchBooksByIsbn(isbn, page, size, sortBy, sortOrder);
        }
        return ResponseEntity.ok(books);
    }
}

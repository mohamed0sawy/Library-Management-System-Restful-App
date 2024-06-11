package com.sawy.LibrarySystem.service;

import com.sawy.LibrarySystem.model.Author;
import com.sawy.LibrarySystem.model.Book;
import com.sawy.LibrarySystem.repository.AuthorRepository;
import com.sawy.LibrarySystem.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorService authorService;

    public Page<Book> getAllBooks(int page, int size, String sortBy, String sortOrder) {
        Sort.Direction direction = sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return bookRepository.findAll(pageable);
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public Book createBook(Book book) {
        Author author = authorService.getAuthorById(book.getAuthorID()).orElse(null);
        book.setAuthor(author);
        return bookRepository.save(book);
    }

    public Optional<Book> updateBook(Long id, Book bookDetails) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            book.setTitle(bookDetails.getTitle());
            book.setIsbn(bookDetails.getIsbn());
            book.setPublicationDate(bookDetails.getPublicationDate());
            book.setGenre(bookDetails.getGenre());
            book.setAvailable(bookDetails.isAvailable());
            book.setAuthorID(bookDetails.getAuthorID());
            book.setAuthor(authorService.getAuthorById(bookDetails.getAuthorID()).orElse(null));
            return Optional.of(bookRepository.save(book));
        } else {
            return Optional.empty();
        }
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public Page<Book> searchBooksByTitle(String title, int page, int size, String sortBy, String sortOrder) {
        Sort.Direction direction = sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return bookRepository.findByTitleContainingIgnoreCase(title, pageable);
    }

    public Page<Book> searchBooksByAuthor(String author, int page, int size, String sortBy, String sortOrder) {
        Sort.Direction direction = sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return bookRepository.findByAuthor_NameContainingIgnoreCase(author, pageable);
    }

    public Page<Book> searchBooksByIsbn(String isbn, int page, int size, String sortBy, String sortOrder) {
        Sort.Direction direction = sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return bookRepository.findByIsbnContainingIgnoreCase(isbn, pageable);
    }
}

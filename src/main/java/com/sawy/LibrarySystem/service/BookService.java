package com.sawy.LibrarySystem.service;

import com.sawy.LibrarySystem.model.Author;
import com.sawy.LibrarySystem.model.Book;
import com.sawy.LibrarySystem.repository.AuthorRepository;
import com.sawy.LibrarySystem.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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

    @Cacheable(value = "books")
    public Page<Book> getAllBooks(int page, int size, String sortBy, String sortOrder) {
        Sort.Direction direction = sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return bookRepository.findAll(pageable);
    }

    @Cacheable(value = "book", key = "#id")
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    @CacheEvict(value = "books", allEntries = true)
    public Book createBook(Book book) {
        Author author = authorService.getAuthorById(book.getAuthorID()).orElse(null);
        book.setAuthor(author);
        return bookRepository.save(book);
    }

    @CacheEvict(value = "books", allEntries = true)
    @CachePut(value = "book", key = "#id")
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

    @Caching(evict = {
            @CacheEvict(value = "books", allEntries = true),
            @CacheEvict(value = "book", key = "#id")
    })
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    @Cacheable(value = "booksByTitle", key = "#title")
    public Page<Book> searchBooksByTitle(String title, int page, int size, String sortBy, String sortOrder) {
        Sort.Direction direction = sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return bookRepository.findByTitleContainingIgnoreCase(title, pageable);
    }

    @Cacheable(value = "booksByAuthor", key = "#author")
    public Page<Book> searchBooksByAuthor(String author, int page, int size, String sortBy, String sortOrder) {
        Sort.Direction direction = sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return bookRepository.findByAuthor_NameContainingIgnoreCase(author, pageable);
    }

    @Cacheable(value = "booksByIsbn", key = "#isbn")
    public Page<Book> searchBooksByIsbn(String isbn, int page, int size, String sortBy, String sortOrder) {
        Sort.Direction direction = sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return bookRepository.findByIsbnContainingIgnoreCase(isbn, pageable);
    }
}

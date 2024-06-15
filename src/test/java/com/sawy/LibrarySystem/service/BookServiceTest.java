package com.sawy.LibrarySystem.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.sawy.LibrarySystem.model.Author;
import com.sawy.LibrarySystem.model.Book;
import com.sawy.LibrarySystem.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorService authorService;

    private BookService bookService;

    @BeforeEach
    void setUp() {
        bookService = new BookService(bookRepository, authorService);
    }

    @Test
    @DisplayName("Get All Books")
    void getAllBooks_pageSizeSortBySortOrder_PageOfBooks() {
        int page = 0;
        int size = 10;
        String sortBy = "title";
        String sortOrder = "asc";
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, sortBy));
        Page<Book> books = new PageImpl<>(List.of(new Book(), new Book()));

        when(bookRepository.findAll(pageable)).thenReturn(books);

        Page<Book> result = bookService.getAllBooks(page, size, sortBy, sortOrder);

        assertEquals(books, result);
        verify(bookRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Get Book By ID")
    void getBookById_ID_Book() {
        Long id = 1L;
        Book book = new Book();
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));

        Optional<Book> result = bookService.getBookById(id);

        assertTrue(result.isPresent());
        assertEquals(book, result.get());
        verify(bookRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Create Book")
    void createBook_Book_Book() {
        Book book = new Book();
        Author author = new Author();
        book.setAuthorID(1L);

        when(authorService.getAuthorById(book.getAuthorID())).thenReturn(Optional.of(author));
        when(bookRepository.save(book)).thenReturn(book);

        Book result = bookService.createBook(book);

        assertEquals(book, result);
        verify(authorService, times(1)).getAuthorById(book.getAuthorID());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    @DisplayName("Update Book")
    void updateBook_IDAndBookDetails_OptionalBook() {
        Long id = 1L;
        Book existingBook = new Book();
        existingBook.setId(id);
        Book updatedBookDetails = new Book();
        updatedBookDetails.setTitle("New Title");
        updatedBookDetails.setAuthorID(2L);
        Author author = new Author();

        when(bookRepository.findById(id)).thenReturn(Optional.of(existingBook));
        when(authorService.getAuthorById(updatedBookDetails.getAuthorID())).thenReturn(Optional.of(author));
        when(bookRepository.save(any(Book.class))).thenReturn(existingBook);

        Optional<Book> result = bookService.updateBook(id, updatedBookDetails);

        assertTrue(result.isPresent());
        assertEquals("New Title", result.get().getTitle());
        verify(bookRepository, times(1)).findById(id);
        verify(authorService, times(1)).getAuthorById(updatedBookDetails.getAuthorID());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    @DisplayName("Delete Book")
    void deleteBook_ID_Void() {
        Long id = 1L;

        bookService.deleteBook(id);

        verify(bookRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Search Book By Title")
    void searchBooksByTitle_TitlePageSizeSortBySortOrder_PageOfBooks() {
        String title = "Test Title";
        int page = 0;
        int size = 10;
        String sortBy = "title";
        String sortOrder = "asc";
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, sortBy));
        Page<Book> books = new PageImpl<>(List.of(new Book(), new Book()));

        when(bookRepository.findByTitleContainingIgnoreCase(title, pageable)).thenReturn(books);

        Page<Book> result = bookService.searchBooksByTitle(title, page, size, sortBy, sortOrder);

        assertEquals(books, result);
        verify(bookRepository, times(1)).findByTitleContainingIgnoreCase(title, pageable);
    }

    @Test
    @DisplayName("Search Book By Author")
    void searchBooksByAuthor_AuthorPageSizeSortBySortOrder_PageOfBooks() {
        String authorName = "Test Author";
        int page = 0;
        int size = 10;
        String sortBy = "title";
        String sortOrder = "asc";
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, sortBy));
        Page<Book> books = new PageImpl<>(List.of(new Book(), new Book()));

        when(bookRepository.findByAuthor_NameContainingIgnoreCase(authorName, pageable)).thenReturn(books);

        Page<Book> result = bookService.searchBooksByAuthor(authorName, page, size, sortBy, sortOrder);

        assertEquals(books, result);
        verify(bookRepository, times(1)).findByAuthor_NameContainingIgnoreCase(authorName, pageable);
    }

    @Test
    @DisplayName("Search Book By ISBN")
    void searchBooksByIsbn_IsbnPageSizeSortBySortOrder_PageOfBooks() {
        String isbn = "1234567890";
        int page = 0;
        int size = 10;
        String sortBy = "title";
        String sortOrder = "asc";
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, sortBy));
        Page<Book> books = new PageImpl<>(List.of(new Book(), new Book()));

        when(bookRepository.findByIsbnContainingIgnoreCase(isbn, pageable)).thenReturn(books);

        Page<Book> result = bookService.searchBooksByIsbn(isbn, page, size, sortBy, sortOrder);

        assertEquals(books, result);
        verify(bookRepository, times(1)).findByIsbnContainingIgnoreCase(isbn, pageable);
    }
}
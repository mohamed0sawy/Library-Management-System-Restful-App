package com.sawy.LibrarySystem.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import com.sawy.LibrarySystem.model.Author;
import com.sawy.LibrarySystem.repository.AuthorRepository;
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

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    private AuthorService authorService;

    @BeforeEach
    void setUp() {
        authorService = new AuthorService(authorRepository);
    }

    @Test
    @DisplayName("Get All Authors")
    void getAllAuthors_pageSizeSortBySortOrder_PageOfAuthors() {
        int page = 0;
        int size = 10;
        String sortBy = "name";
        String sortOrder = "asc";
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, sortBy));
        Page<Author> authors = new PageImpl<>(List.of(new Author(), new Author()));

        when(authorRepository.findAll(pageable)).thenReturn(authors);

        Page<Author> result = authorService.getAllAuthors(page, size, sortBy, sortOrder);

        assertEquals(authors, result);
        verify(authorRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Get Author By ID")
    void getAuthorById_ID_Author() {
        Long id = 1L;
        Author author = new Author();
        when(authorRepository.findById(id)).thenReturn(Optional.of(author));

        Optional<Author> result = authorService.getAuthorById(id);

        assertTrue(result.isPresent());
        assertEquals(author, result.get());
        verify(authorRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Create Author")
    void createAuthor_Author_Author() {
        Author author = new Author();
        when(authorRepository.save(author)).thenReturn(author);

        Author result = authorService.createAuthor(author);

        assertEquals(author, result);
        verify(authorRepository, times(1)).save(author);
    }

    @Test
    @DisplayName("Update Author")
    void updateAuthor_IDAndAuthorDetails_OptionalAuthor() {
        Long id = 1L;
        Author existingAuthor = new Author();
        existingAuthor.setId(id);
        Author updatedAuthorDetails = new Author();
        updatedAuthorDetails.setName("New Name");

        when(authorRepository.findById(id)).thenReturn(Optional.of(existingAuthor));
        when(authorRepository.save(any(Author.class))).thenReturn(existingAuthor);

        Optional<Author> result = authorService.updateAuthor(id, updatedAuthorDetails);

        assertTrue(result.isPresent());
        assertEquals("New Name", result.get().getName());
        verify(authorRepository, times(1)).findById(id);
        verify(authorRepository, times(1)).save(any(Author.class));
    }

    @Test
    @DisplayName("Delete Author")
    void deleteAuthor_ID_Void() {
        Long id = 1L;

        authorService.deleteAuthor(id);

        verify(authorRepository, times(1)).deleteById(id);
    }
}
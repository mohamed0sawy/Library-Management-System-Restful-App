package com.sawy.LibrarySystem.repository;

import com.sawy.LibrarySystem.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Page<Book> findByAuthor_NameContainingIgnoreCase(String author, Pageable pageable);
    Page<Book> findByIsbnContainingIgnoreCase(String isbn, Pageable pageable);
}

package com.sawy.LibrarySystem.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is mandatory")
    private String title;

    @NotBlank(message = "ISBN is mandatory")
    private String isbn;

    @PastOrPresent(message = "Publication date cannot be in the future")
    private LocalDate publicationDate;

    @NotBlank(message = "Genre is mandatory")
    private String genre;

    private boolean available;

    @NotNull(message = "Author ID is mandatory")
    private Long authorID;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = true)
    @JsonIgnoreProperties("books")
    private Author author;

    @OneToMany(mappedBy = "book")
    @JsonIgnoreProperties({"customer", "book"})
    private List<BorrowingRecord> borrowingRecords;

    public Book(String title, String isbn, LocalDate publicationDate, String genre, boolean available, Long authorID) {
        this.title = title;
        this.isbn = isbn;
        this.publicationDate = publicationDate;
        this.genre = genre;
        this.available = available;
        this.authorID = authorID;
    }
}

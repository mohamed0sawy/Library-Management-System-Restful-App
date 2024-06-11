package com.sawy.LibrarySystem.model;

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
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is mandatory")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @Past(message = "Birthdate must be a past date")
    private LocalDate birthDate;

    @NotBlank(message = "Nationality is mandatory")
    @Size(min = 4, max = 50, message = "Nationality must be between 4 and 50 characters")
    private String nationality;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"author", "borrowingRecords", "authorID"})
    private List<Book> books;

    public Author(String name, LocalDate birthDate, String nationality) {
        this.name = name;
        this.birthDate = birthDate;
        this.nationality = nationality;
    }
}

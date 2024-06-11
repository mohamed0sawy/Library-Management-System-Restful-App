package com.sawy.LibrarySystem.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class BorrowingRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"borrowingRecords", "password"})
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "book_id")
    @JsonIgnoreProperties({"borrowingRecords", "author", "authorID"})
    private Book book;

    @NotNull(message = "Customer ID is mandatory")
    private Long customerID;

    @NotNull(message = "Book ID is mandatory")
    private Long bookID;

//    @PastOrPresent(message = "Borrow date cannot be in the future")
    private LocalDate borrowDate;

//    @FutureOrPresent(message = "Return date cannot be in the past")
    private LocalDate returnDate;

    public BorrowingRecord(Customer customer, Book book, LocalDate borrowDate, LocalDate returnDate, Long customerID, Long bookID) {
        this.customer = customer;
        this.book = book;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.customerID = customerID;
        this.bookID = bookID;
    }

    @AssertTrue(message = "Return date must be after borrow date")
    private boolean isReturnDateValid() {
        if (borrowDate != null && returnDate != null) {
            return returnDate.isAfter(borrowDate) || returnDate.isEqual(borrowDate);
        }
        return true;
    }
}

//package com.sawy.LibrarySystem.populator;
//
//import com.sawy.LibrarySystem.model.Author;
//import com.sawy.LibrarySystem.model.Book;
//import com.sawy.LibrarySystem.model.BorrowingRecord;
//import com.sawy.LibrarySystem.model.Customer;
//import com.sawy.LibrarySystem.service.AuthorService;
//import com.sawy.LibrarySystem.service.BookService;
//import com.sawy.LibrarySystem.service.BorrowingRecordService;
//import com.sawy.LibrarySystem.service.CustomerService;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//import java.time.LocalDate;
//
//@Component
//public class DatabasePopulator implements CommandLineRunner {
//
//    private final AuthorService authorService;
//    private final BookService bookService;
//    private final CustomerService customerService;
//    private final BorrowingRecordService borrowingRecordService;
//
//    public DatabasePopulator(AuthorService authorService, BookService bookService,
//                             CustomerService customerService, BorrowingRecordService borrowingRecordService) {
//        this.authorService = authorService;
//        this.bookService = bookService;
//        this.customerService = customerService;
//        this.borrowingRecordService = borrowingRecordService;
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//        populateAuthors();
//        populateBooks();
//        populateCustomers();
//        populateBorrowingRecords();
//    }
//
//    private void populateAuthors() {
//        Author author1 = new Author("Mo sawy", LocalDate.of(1999, 4, 4), "Egyptian");
//        Author author2 = new Author("john doe", LocalDate.of(1989, 8, 25), "British");
//
//        authorService.createAuthor(author1);
//        authorService.createAuthor(author2);
//    }
//
//    private void populateBooks() {
//        Author author1 = authorService.getAuthorById(1L).orElse(null);
//        Author author2 = authorService.getAuthorById(2L).orElse(null);
//
//        if (author1 != null && author2 != null) {
//            Book book1 = new Book("Intro to java", "978-014-330-529",
//                    LocalDate.of(2020, 10, 1), "Educational", true, author1);
//            Book book2 = new Book("master sql", "345-009-307-313",
//                    LocalDate.of(2017, 5, 15), "Science", true, author2);
//
//            bookService.createBook(book1);
//            bookService.createBook(book2);
//        }
//    }
//
//    private void populateCustomers() {
//        Customer customer1 = new Customer("omar", "omar@example.com",
//                "123 Main St", "01234567893", "password123");
//        Customer customer2 = new Customer("adel", "adel@example.com",
//                "456 saad St", "01028478332", "password456");
//
//        customerService.createCustomer(customer1);
//        customerService.createCustomer(customer2);
//    }
//
//    private void populateBorrowingRecords() {
//        Book book1 = bookService.getBookById(1L).orElse(null);
//        Book book2 = bookService.getBookById(2L).orElse(null);
//        Customer customer1 = customerService.getCustomerById(1L).orElse(null);
//        Customer customer2 = customerService.getCustomerById(2L).orElse(null);
//
//        if (book1 != null && book2 != null && customer1 != null && customer2 != null) {
//            BorrowingRecord record1 = new BorrowingRecord(customer1, book1, LocalDate.of(2024, 6, 1),
//                    LocalDate.of(2024, 7, 1));
//            BorrowingRecord record2 = new BorrowingRecord(customer2, book2, LocalDate.of(2024, 6, 5),
//                    LocalDate.of(2024, 7, 5));
//
//            borrowingRecordService.createBorrowingRecord(record1);
//            borrowingRecordService.createBorrowingRecord(record2);
//        }
//    }
//}
//

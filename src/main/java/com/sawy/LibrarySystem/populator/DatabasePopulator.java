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
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Component;
//import java.time.LocalDate;
//import java.time.Month;
//
//@Component
//public class DatabasePopulator implements CommandLineRunner {
//
//    private final AuthorService authorService;
//    private final BookService bookService;
//    private final CustomerService customerService;
//    private final BorrowingRecordService borrowingRecordService;
//    private final BCryptPasswordEncoder passwordEncoder;
//
//    @Autowired
//    public DatabasePopulator(AuthorService authorService, BookService bookService, CustomerService customerService, BorrowingRecordService borrowingRecordService, BCryptPasswordEncoder passwordEncoder) {
//        this.authorService = authorService;
//        this.bookService = bookService;
//        this.customerService = customerService;
//        this.borrowingRecordService = borrowingRecordService;
//        this.passwordEncoder = passwordEncoder;
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
//        Author author1 = new Author("ali", LocalDate.of(1995, Month.JULY, 31), "syrian");
//        Author author2 = new Author("george orwell", LocalDate.of(1930, Month.JUNE, 25), "British");
//        authorService.createAuthor(author1);
//        authorService.createAuthor(author2);
//    }
//
//    private void populateBooks() {
//        Author author1 = authorService.getAuthorById(1L).orElseThrow();
//        Author author2 = authorService.getAuthorById(2L).orElseThrow();
//
//        Book book1 = new Book("be happy", "978-0747532743", LocalDate.of(2015, Month.JUNE, 26), "life-coaching", true, author1.getId());
//        Book book2 = new Book("1984", "978-0452284234", LocalDate.of(1960, Month.JUNE, 8), "political", true, author2.getId());
//        bookService.createBook(book1);
//        bookService.createBook(book2);
//    }
//
//    private void populateCustomers() {
//        String encodedPassword = passwordEncoder.encode("password123");
//        Customer customer1 = new Customer("sawy", "sawy@example.com", "123 Main St", "01234567893", encodedPassword);
//        String encodedPassword2 = passwordEncoder.encode("password456");
//        Customer customer2 = new Customer("Jane Smith", "jane@example.com", "456 Elm St", "01234567804", encodedPassword2);
//        customerService.createCustomer(customer1);
//        customerService.createCustomer(customer2);
//    }
//
//    private void populateBorrowingRecords() {
//        Customer customer1 = customerService.getCustomerById(1L).orElseThrow();
//        Customer customer2 = customerService.getCustomerById(2L).orElseThrow();
//        Book book1 = bookService.getBookById(1L).orElseThrow();
//        Book book2 = bookService.getBookById(2L).orElseThrow();
//
//        BorrowingRecord borrowingRecord1 = new BorrowingRecord(customer1, book1, LocalDate.now().minusDays(10), LocalDate.now().plusDays(20), customer1.getId(), book1.getId());
//        BorrowingRecord borrowingRecord2 = new BorrowingRecord(customer2, book2, LocalDate.now().minusDays(5), LocalDate.now().plusDays(15), customer2.getId(), book2.getId());
//        borrowingRecordService.createBorrowingRecord(borrowingRecord1);
//        borrowingRecordService.createBorrowingRecord(borrowingRecord2);
//    }
//}

package com.sawy.LibrarySystem.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sawy.LibrarySystem.model.Author;
import com.sawy.LibrarySystem.model.Book;
import com.sawy.LibrarySystem.model.BorrowingRecord;
import com.sawy.LibrarySystem.model.Customer;
import com.sawy.LibrarySystem.service.BookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
    @DisplayName("Get All Books")
    void getAllBooks_GetRequest_ReturnsPageOfBooks() throws Exception {
        int page = 0;
        int size = 10;
        String sortBy = "id";
        String sortOrder = "asc";

        Page<Book> books = createMockBooksPage();
        when(bookService.getAllBooks(page, size, sortBy, sortOrder)).thenReturn(books);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("sortBy", sortBy)
                        .param("sortOrder", sortOrder))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].title").value(books.getContent().get(0).getTitle()));

        verify(bookService, times(1)).getAllBooks(page, size, sortBy, sortOrder);
    }

    @Test
    @DisplayName("Get Book By Existing ID")
    void getBookById_ExistingId_ReturnsBook() throws Exception {
        Long id = 1L;

        Book book = new Book();
        book.setId(id);
        book.setTitle("Sample Book");
        when(bookService.getBookById(id)).thenReturn(Optional.of(book));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(book.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(book.getTitle()));

        verify(bookService, times(1)).getBookById(id);
    }

    @Test
    @DisplayName("Get Book By Non-Existing ID")
    void getBookById_NonExistingId_ReturnsNotFound() throws Exception {
        Long id = 88L;

        when(bookService.getBookById(id)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(bookService, times(1)).getBookById(id);
    }

    @Test
    @DisplayName("Create Book")
    void createBook_Book_ReturnsCreatedBook() throws Exception {
        Book requestBook = new Book();
        requestBook.setTitle("New Book");

        Book createdBook = new Book();
        createdBook.setId(1L);
        createdBook.setTitle(requestBook.getTitle());
        when(bookService.createBook(any(Book.class))).thenReturn(createdBook);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/books")
                        .content(asJsonString(requestBook))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(createdBook.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(createdBook.getTitle()));

        verify(bookService, times(1)).createBook(any(Book.class));
    }

    @Test
    @DisplayName("Update Existing Book")
    void updateBook_IdAndBook_ReturnsUpdatedBook() throws Exception {
        Long id = 1L;

        Book requestBook = new Book();
        requestBook.setTitle("Updated Book");

        Book updatedBook = new Book();
        updatedBook.setId(id);
        updatedBook.setTitle(requestBook.getTitle());
        when(bookService.updateBook(eq(id), any(Book.class))).thenReturn(Optional.of(updatedBook));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/books/{id}", id)
                        .content(asJsonString(requestBook))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(updatedBook.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(updatedBook.getTitle()));

        verify(bookService, times(1)).updateBook(eq(id), any(Book.class));
    }

    @Test
    @DisplayName("Update Non-Existing Book")
    void updateBook_NonExistingId_ReturnsNotFound() throws Exception {
        Long id = 88L;

        Book requestBook = new Book();
        requestBook.setTitle("Updated Book");

        when(bookService.updateBook(eq(id), any(Book.class))).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/books/{id}", id)
                        .content(asJsonString(requestBook))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(bookService, times(1)).updateBook(eq(id), any(Book.class));
    }

    @Test
    @DisplayName("Delete Existing Book")
    void deleteBook_ExistingId_ReturnsNoContent() throws Exception {
        Long id = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/books/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(bookService, times(1)).deleteBook(id);
    }

    @Test
    @DisplayName("Search Books - By Title")
    void searchBooks_ByTitle_ReturnsPageOfBooks() throws Exception {
        String title = "Sample Title";
        int page = 0;
        int size = 10;
        String sortBy = "id";
        String sortOrder = "asc";

        Page<Book> books = createMockBooksPage();
        when(bookService.searchBooksByTitle(title, page, size, sortBy, sortOrder)).thenReturn(books);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books/search")
                        .param("title", title)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("sortBy", sortBy)
                        .param("sortOrder", sortOrder))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].title").value(books.getContent().get(0).getTitle()));

        verify(bookService, times(1)).searchBooksByTitle(title, page, size, sortBy, sortOrder);
    }

    @Test
    @DisplayName("Search Books - By Author")
    void searchBooks_ByAuthor_ReturnsPageOfBooks() throws Exception {
        String author = "Sample Author";
        int page = 0;
        int size = 10;
        String sortBy = "id";
        String sortOrder = "asc";

        Page<Book> books = createMockBooksPage();
        when(bookService.searchBooksByAuthor(author, page, size, sortBy, sortOrder)).thenReturn(books);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books/search")
                        .param("author", author)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("sortBy", sortBy)
                        .param("sortOrder", sortOrder))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].title").value(books.getContent().get(0).getTitle()));

        verify(bookService, times(1)).searchBooksByAuthor(author, page, size, sortBy, sortOrder);
    }

    @Test
    @DisplayName("Search Books - By ISBN")
    void searchBooks_ByIsbn_ReturnsPageOfBooks() throws Exception {
        String isbn = "Sample ISBN";
        int page = 0;
        int size = 10;
        String sortBy = "id";
        String sortOrder = "asc";

        Page<Book> books = createMockBooksPage();
        when(bookService.searchBooksByIsbn(isbn, page, size, sortBy, sortOrder)).thenReturn(books);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books/search")
                        .param("isbn", isbn)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("sortBy", sortBy)
                        .param("sortOrder", sortOrder))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].title").value(books.getContent().get(0).getTitle()));

        verify(bookService, times(1)).searchBooksByIsbn(isbn, page, size, sortBy, sortOrder);
    }

    @Test
    @DisplayName("Search Books - Bad Request")
    void searchBooks_BadRequest_ReturnsBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books/search"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        verifyNoInteractions(bookService);
    }

    private String asJsonString(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private Page<Book> createMockBooksPage() {
        List<Book> books = new ArrayList<>();
        Author author = new Author("sawy", LocalDate.of(1980, Month.APRIL, 1), "Egyptian");
        author.setId(1L);

        Customer customer1 = new Customer("John Doe", "john@example.com", "Address 1", "12345678", "password");
        customer1.setId(1L);
        Customer customer2 = new Customer("Jane Smith", "jane@example.com", "Address 2", "98765432", "password");
        customer2.setId(2L);

        Book book1 = new Book("intro to java", "972-375632-274", LocalDate.of(2012, Month.APRIL, 13), "Educational", true, author.getId());
        book1.setId(1L);
        book1.setAuthor(author);

        Book book2 = new Book("intro to python", "972-375632-274", LocalDate.of(2012, Month.APRIL, 6), "Educational", true, author.getId());
        book2.setId(2L);
        book2.setAuthor(author);

        books.add(book1);
        books.add(book2);

        BorrowingRecord record1 = new BorrowingRecord(customer1, book1, LocalDate.of(2024, Month.JUNE, 15),
                LocalDate.of(2024, Month.JUNE,25),1L,1L);
        BorrowingRecord record2 = new BorrowingRecord(customer2, book2, LocalDate.of(2024, Month.JUNE, 15),
                LocalDate.of(2024, Month.JUNE,25),2L,2L);

        book1.setBorrowingRecords(List.of(record1));
        book2.setBorrowingRecords(List.of(record2));
//        book1.getBorrowingRecords().add(record1);
//        book2.getBorrowingRecords().add(record2);
        return new PageImpl<>(books, PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id")), books.size());
    }
}
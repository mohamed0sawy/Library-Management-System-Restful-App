package com.sawy.LibrarySystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sawy.LibrarySystem.model.Author;
import com.sawy.LibrarySystem.model.Book;
import com.sawy.LibrarySystem.model.BorrowingRecord;
import com.sawy.LibrarySystem.model.Customer;
import com.sawy.LibrarySystem.service.CustomerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
import static org.mockito.Mockito.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Test
    @DisplayName("Get All Customers")
    void getAllCustomers_GetRequest_ReturnsPageOfCustomers() throws Exception {
        int page = 0;
        int size = 10;
        String sortBy = "id";
        String sortOrder = "asc";

        Page<Customer> customers = createMockCustomersPage();
        when(customerService.getAllCustomers(page, size, sortBy, sortOrder)).thenReturn(customers);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/customers")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("sortBy", sortBy)
                        .param("sortOrder", sortOrder))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name").value(customers.getContent().get(0).getName()));

        verify(customerService, times(1)).getAllCustomers(page, size, sortBy, sortOrder);
    }

    @Test
    @DisplayName("Get Customer By ID")
    void getCustomerById_ExistingId_ReturnsCustomer() throws Exception {
        Long id = 1L;

        Customer customer = new Customer("John Doe", "john@example.com", "Address 1", "12345678", "password");
        customer.setId(id);
        when(customerService.getCustomerById(id)).thenReturn(Optional.of(customer));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/customers/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(customer.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(customer.getName()));

        verify(customerService, times(1)).getCustomerById(id);
    }

    @Test
    @DisplayName("Get Customer By ID if not exist")
    void getCustomerById_NonExistingId_ReturnsNotFound() throws Exception {
        Long id = 88L;

        when(customerService.getCustomerById(id)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/customers/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(customerService, times(1)).getCustomerById(id);
    }

    @Test
    @DisplayName("Create Customer")
    void createCustomer_Customer_ReturnsCreatedCustomer() throws Exception {
        Customer requestCustomer = new Customer("Jane Smith", "jane@example.com", "Address 2", "98765432", "password");

        Customer createdCustomer = new Customer("Jane Smith", "jane@example.com", "Address 2", "98765432", "password");
        createdCustomer.setId(1L);
        when(customerService.createCustomer(any(Customer.class))).thenReturn(createdCustomer);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/customers")
                        .content(asJsonString(requestCustomer))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(createdCustomer.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(createdCustomer.getName()));

        verify(customerService, times(1)).createCustomer(any(Customer.class));
    }

    @Test
    @DisplayName("Update Existing Customer")
    void updateCustomer_IdAndCustomer_ReturnsUpdatedCustomer() throws Exception {
        Long id = 1L;

        Customer requestCustomer = new Customer("Updated Name", "updated@example.com", "Updated Address", "12345678", "updatedPassword");

        Customer updatedCustomer = new Customer("Updated Name", "updated@example.com", "Updated Address", "12345678", "updatedPassword");
        updatedCustomer.setId(id);
        when(customerService.updateCustomer(eq(id), any(Customer.class))).thenReturn(Optional.of(updatedCustomer));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/customers/{id}", id)
                        .content(asJsonString(requestCustomer))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(updatedCustomer.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(updatedCustomer.getName()));

        verify(customerService, times(1)).updateCustomer(eq(id), any(Customer.class));
    }

    @Test
    @DisplayName("Update Non-Existing Customer")
    void updateCustomer_NonExistingId_ReturnsNotFound() throws Exception {
        Long id = 88L;

        Customer requestCustomer = new Customer("Updated Name", "updated@example.com", "Updated Address", "12345678", "updatedPassword");

        when(customerService.updateCustomer(eq(id), any(Customer.class))).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/customers/{id}", id)
                        .content(asJsonString(requestCustomer))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(customerService, times(1)).updateCustomer(eq(id), any(Customer.class));
    }

    @Test
    @DisplayName("Delete Existing Customer")
    void deleteCustomer_ExistingId_ReturnsNoContent() throws Exception {
        Long id = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/customers/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(customerService, times(1)).deleteCustomer(id);
    }

    private String asJsonString(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Page<Customer> createMockCustomersPage() {
        List<Customer> customers = new ArrayList<>();
//        customers.add(new Customer("John Doe", "john@example.com", "Address 1", "12345678", "password"));
//        customers.add(new Customer("Jane Smith", "jane@example.com", "Address 2", "98765432", "password"));

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

        customer1.setBorrowingRecords(List.of(record1));
        customer2.setBorrowingRecords(List.of(record2));
        customers.add(customer1);
        customers.add(customer2);

        return new PageImpl<>(customers, PageRequest.of(0, 10, org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.ASC, "id")), customers.size());
    }
}
package com.sawy.LibrarySystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sawy.LibrarySystem.controller.BorrowingRecordController;
import com.sawy.LibrarySystem.model.Book;
import com.sawy.LibrarySystem.model.BorrowingRecord;
import com.sawy.LibrarySystem.model.Customer;
import com.sawy.LibrarySystem.service.BorrowingRecordService;
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

import static org.mockito.Mockito.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@WebMvcTest(BorrowingRecordController.class)
class BorrowingRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BorrowingRecordService borrowingRecordService;

    @Test
    @DisplayName("Get All Borrowing Records")
    void getAllBorrowingRecords_GetRequest_ReturnsPageOfBorrowingRecords() throws Exception {
        int page = 0;
        int size = 10;
        String sortBy = "id";
        String sortOrder = "asc";

        Page<BorrowingRecord> borrowingRecords = createMockBorrowingRecordsPage();
        when(borrowingRecordService.getAllBorrowingRecords(page, size, sortBy, sortOrder)).thenReturn(borrowingRecords);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/borrowings")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("sortBy", sortBy)
                        .param("sortOrder", sortOrder))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].customerID").value(borrowingRecords.getContent().get(0).getCustomer().getId()));

        verify(borrowingRecordService, times(1)).getAllBorrowingRecords(page, size, sortBy, sortOrder);
    }

    @Test
    @DisplayName("Get Borrowing Record By ID")
    void getBorrowingRecordById_ExistingId_ReturnsBorrowingRecord() throws Exception {
        Long id = 1L;

        BorrowingRecord borrowingRecord = createMockBorrowingRecord();
        when(borrowingRecordService.getBorrowingRecordById(id)).thenReturn(Optional.of(borrowingRecord));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/borrowings/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(borrowingRecord.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerID").value(borrowingRecord.getCustomer().getId()));

        verify(borrowingRecordService, times(1)).getBorrowingRecordById(id);
    }

    @Test
    @DisplayName("Get Borrowing Record By ID if not exist")
    void getBorrowingRecordById_NonExistingId_ReturnsNotFound() throws Exception {
        Long id = 88L;

        when(borrowingRecordService.getBorrowingRecordById(id)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/borrowings/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(borrowingRecordService, times(1)).getBorrowingRecordById(id);
    }

    @Test
    @DisplayName("Create Borrowing Record")
    void createBorrowingRecord_BorrowingRecord_ReturnsCreatedBorrowingRecord() throws Exception {
        BorrowingRecord requestBorrowingRecord = createMockBorrowingRecord();

        BorrowingRecord createdBorrowingRecord = createMockBorrowingRecord();
        createdBorrowingRecord.setId(1L);
        when(borrowingRecordService.createBorrowingRecord(any(BorrowingRecord.class))).thenReturn(createdBorrowingRecord);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/borrowings")
                        .content(asJsonString(requestBorrowingRecord))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(createdBorrowingRecord.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerID").value(createdBorrowingRecord.getCustomer().getId()));

        verify(borrowingRecordService, times(1)).createBorrowingRecord(any(BorrowingRecord.class));
    }

    @Test
    @DisplayName("Update Existing Borrowing Record")
    void updateBorrowingRecord_IdAndBorrowingRecord_ReturnsUpdatedBorrowingRecord() throws Exception {
        Long id = 1L;

        BorrowingRecord requestBorrowingRecord = createMockBorrowingRecord();

        BorrowingRecord updatedBorrowingRecord = createMockBorrowingRecord();
        updatedBorrowingRecord.setId(id);
        when(borrowingRecordService.updateBorrowingRecord(eq(id), any(BorrowingRecord.class))).thenReturn(Optional.of(updatedBorrowingRecord));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/borrowings/{id}", id)
                        .content(asJsonString(requestBorrowingRecord))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(updatedBorrowingRecord.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerID").value(updatedBorrowingRecord.getCustomer().getId()));

        verify(borrowingRecordService, times(1)).updateBorrowingRecord(eq(id), any(BorrowingRecord.class));
    }

    @Test
    @DisplayName("Update Non-Existing Borrowing Record")
    void updateBorrowingRecord_NonExistingId_ReturnsNotFound() throws Exception {
        Long id = 88L;

        BorrowingRecord requestBorrowingRecord = createMockBorrowingRecord();

        when(borrowingRecordService.updateBorrowingRecord(eq(id), any(BorrowingRecord.class))).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/borrowings/{id}", id)
                        .content(asJsonString(requestBorrowingRecord))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(borrowingRecordService, times(1)).updateBorrowingRecord(eq(id), any(BorrowingRecord.class));
    }

    @Test
    @DisplayName("Delete Existing Borrowing Record")
    void deleteBorrowingRecord_ExistingId_ReturnsNoContent() throws Exception {
        Long id = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/borrowings/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(borrowingRecordService, times(1)).deleteBorrowingRecord(id);
    }

    private String asJsonString(Object object) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Page<BorrowingRecord> createMockBorrowingRecordsPage() {
        List<BorrowingRecord> borrowingRecords = new ArrayList<>();
        borrowingRecords.add(createMockBorrowingRecord());

        return new PageImpl<>(borrowingRecords, PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id")), borrowingRecords.size());
    }

    private BorrowingRecord createMockBorrowingRecord() {
        Customer customer = new Customer("John Doe", "john@example.com", "Address 1", "12345678", "password");
        customer.setId(1L);
        Book book = new Book("Intro to Java", "972-375632-274", LocalDate.of(2012, Month.APRIL, 13), "Educational", true, customer.getId());
        book.setId(1L);

        return new BorrowingRecord(customer, book, LocalDate.now(), LocalDate.now().plusDays(10), customer.getId(), book.getId());
    }
}
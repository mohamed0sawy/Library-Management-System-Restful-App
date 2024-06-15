package com.sawy.LibrarySystem.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import com.sawy.LibrarySystem.model.Book;
import com.sawy.LibrarySystem.model.BorrowingRecord;
import com.sawy.LibrarySystem.model.Customer;
import com.sawy.LibrarySystem.repository.BorrowingRecordRepository;
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

import java.util.List;

@ExtendWith(MockitoExtension.class)
class BorrowingRecordServiceTest {

    @Mock
    private BorrowingRecordRepository borrowingRecordRepository;

    @Mock
    private CustomerService customerService;

    @Mock
    private BookService bookService;

    private BorrowingRecordService borrowingRecordService;

    @BeforeEach
    void setUp() {
        borrowingRecordService = new BorrowingRecordService(borrowingRecordRepository, customerService, bookService);
    }

    @Test
    @DisplayName("Get All Borrowing Records")
    void getAllBorrowingRecords_pageSizeSortBySortOrder_PageOfBorrowingRecords() {
        int page = 0;
        int size = 10;
        String sortBy = "borrowDate";
        String sortOrder = "asc";
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, sortBy));
        Page<BorrowingRecord> borrowingRecords = new PageImpl<>(List.of(new BorrowingRecord(), new BorrowingRecord()));

        when(borrowingRecordRepository.findAll(pageable)).thenReturn(borrowingRecords);

        Page<BorrowingRecord> result = borrowingRecordService.getAllBorrowingRecords(page, size, sortBy, sortOrder);

        assertEquals(borrowingRecords, result);
        verify(borrowingRecordRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Get Borrowing Record By ID")
    void getBorrowingRecordById_ID_BorrowingRecord() {
        Long id = 1L;
        BorrowingRecord borrowingRecord = new BorrowingRecord();
        when(borrowingRecordRepository.findById(id)).thenReturn(Optional.of(borrowingRecord));

        Optional<BorrowingRecord> result = borrowingRecordService.getBorrowingRecordById(id);

        assertTrue(result.isPresent());
        assertEquals(borrowingRecord, result.get());
        verify(borrowingRecordRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Create Borrowing Record")
    void createBorrowingRecord_BorrowingRecord_BorrowingRecord() {
        BorrowingRecord borrowingRecord = new BorrowingRecord();
        borrowingRecord.setCustomerID(1L);
        borrowingRecord.setBookID(2L);

        Customer customer = new Customer();
        Book book = new Book();

        when(customerService.getCustomerById(borrowingRecord.getCustomerID())).thenReturn(Optional.of(customer));
        when(bookService.getBookById(borrowingRecord.getBookID())).thenReturn(Optional.of(book));
        when(borrowingRecordRepository.save(borrowingRecord)).thenReturn(borrowingRecord);

        BorrowingRecord result = borrowingRecordService.createBorrowingRecord(borrowingRecord);

        assertEquals(borrowingRecord, result);
        assertEquals(customer, borrowingRecord.getCustomer());
        assertEquals(book, borrowingRecord.getBook());
        verify(customerService, times(1)).getCustomerById(borrowingRecord.getCustomerID());
        verify(bookService, times(1)).getBookById(borrowingRecord.getBookID());
        verify(borrowingRecordRepository, times(1)).save(borrowingRecord);
    }

    @Test
    @DisplayName("Update Borrowing Record")
    void updateBorrowingRecord_IDAndBorrowingRecordDetails_OptionalBorrowingRecord() {
        Long id = 1L;
        BorrowingRecord existingBorrowingRecord = new BorrowingRecord();
        existingBorrowingRecord.setId(id);
        BorrowingRecord updatedBorrowingRecordDetails = new BorrowingRecord();
        updatedBorrowingRecordDetails.setCustomerID(1L);
        updatedBorrowingRecordDetails.setBookID(2L);

        Customer customer = new Customer();
        Book book = new Book();

        when(borrowingRecordRepository.findById(id)).thenReturn(Optional.of(existingBorrowingRecord));
        when(customerService.getCustomerById(updatedBorrowingRecordDetails.getCustomerID())).thenReturn(Optional.of(customer));
        when(bookService.getBookById(updatedBorrowingRecordDetails.getBookID())).thenReturn(Optional.of(book));
        when(borrowingRecordRepository.save(any(BorrowingRecord.class))).thenReturn(existingBorrowingRecord);

        Optional<BorrowingRecord> result = borrowingRecordService.updateBorrowingRecord(id, updatedBorrowingRecordDetails);

        assertTrue(result.isPresent());
        assertEquals(customer, result.get().getCustomer());
        assertEquals(book, result.get().getBook());
        verify(borrowingRecordRepository, times(1)).findById(id);
        verify(customerService, times(1)).getCustomerById(updatedBorrowingRecordDetails.getCustomerID());
        verify(bookService, times(1)).getBookById(updatedBorrowingRecordDetails.getBookID());
        verify(borrowingRecordRepository, times(1)).save(any(BorrowingRecord.class));
    }

    @Test
    @DisplayName("Delete Borrowing Record")
    void deleteBorrowingRecord_ID_Void() {
        Long id = 1L;

        borrowingRecordService.deleteBorrowingRecord(id);

        verify(borrowingRecordRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Get Borrowing Record By Customer ID")
    void getBorrowingRecordsByCustomerId_UserId_PageOfBorrowingRecords() {
        Long userId = 1L;
        int page = 0;
        int size = 10;
        String sortBy = "borrowDate";
        String sortOrder = "asc";
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, sortBy));
        Page<BorrowingRecord> borrowingRecords = new PageImpl<>(List.of(new BorrowingRecord(), new BorrowingRecord()));

        when(borrowingRecordRepository.findByCustomer_Id(userId, pageable)).thenReturn(borrowingRecords);

        Page<BorrowingRecord> result = borrowingRecordService.getBorrowingRecordsByCustomerId(userId, page, size, sortBy, sortOrder);

        assertEquals(borrowingRecords, result);
        verify(borrowingRecordRepository, times(1)).findByCustomer_Id(userId, pageable);
    }

    @Test
    @DisplayName("Get Borrowing Record By Book ID")
    void getBorrowingRecordsByBookId_BookId_PageOfBorrowingRecords() {
        Long bookId = 1L;
        int page = 0;
        int size = 10;
        String sortBy = "borrowDate";
        String sortOrder = "asc";
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, sortBy));
        Page<BorrowingRecord> borrowingRecords = new PageImpl<>(List.of(new BorrowingRecord(), new BorrowingRecord()));

        when(borrowingRecordRepository.findByBook_Id(bookId, pageable)).thenReturn(borrowingRecords);

        Page<BorrowingRecord> result = borrowingRecordService.getBorrowingRecordsByBookId(bookId, page, size, sortBy, sortOrder);

        assertEquals(borrowingRecords, result);
        verify(borrowingRecordRepository, times(1)).findByBook_Id(bookId, pageable);
    }
}
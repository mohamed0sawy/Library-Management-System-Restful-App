package com.sawy.LibrarySystem.service;

import com.sawy.LibrarySystem.model.Book;
import com.sawy.LibrarySystem.model.BorrowingRecord;
import com.sawy.LibrarySystem.model.Customer;
import com.sawy.LibrarySystem.repository.BorrowingRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BorrowingRecordService {

    private final BorrowingRecordRepository borrowingRecordRepository;
    private final CustomerService customerService;
    private final BookService bookService;

    @Cacheable(value = "borrowingRecords")
    public Page<BorrowingRecord> getAllBorrowingRecords(int page, int size, String sortBy, String sortOrder) {
        Sort.Direction direction = sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return borrowingRecordRepository.findAll(pageable);
    }

    @Cacheable(value = "borrowingRecord", key = "#id")
    public Optional<BorrowingRecord> getBorrowingRecordById(Long id) {
        return borrowingRecordRepository.findById(id);
    }

    @CacheEvict(value = "borrowingRecords", allEntries = true)
    public BorrowingRecord createBorrowingRecord(BorrowingRecord borrowingRecord) {
        Customer customer = customerService.getCustomerById(borrowingRecord.getCustomerID()).orElse(null);
        Book book = bookService.getBookById(borrowingRecord.getBookID()).orElse(null);
        borrowingRecord.setCustomer(customer);
        borrowingRecord.setBook(book);
        return borrowingRecordRepository.save(borrowingRecord);
    }

    @CacheEvict(value = "borrowingRecords", allEntries = true)
    @CachePut(value = "borrowingRecord", key = "#id")
    public Optional<BorrowingRecord> updateBorrowingRecord(Long id, BorrowingRecord borrowingRecordDetails) {
        Optional<BorrowingRecord> borrowingRecordOptional = borrowingRecordRepository.findById(id);
        if (borrowingRecordOptional.isPresent()) {
            BorrowingRecord borrowingRecord = borrowingRecordOptional.get();
            borrowingRecord.setCustomer(customerService.getCustomerById(borrowingRecordDetails.getCustomerID()).orElse(null));
            borrowingRecord.setBook(bookService.getBookById(borrowingRecordDetails.getBookID()).orElse(null));
            borrowingRecord.setBorrowDate(borrowingRecordDetails.getBorrowDate());
            borrowingRecord.setReturnDate(borrowingRecordDetails.getReturnDate());
            return Optional.of(borrowingRecordRepository.save(borrowingRecord));
        } else {
            return Optional.empty();
        }
    }

    @Caching(evict = {
            @CacheEvict(value = "borrowingRecords", allEntries = true),
            @CacheEvict(value = "borrowingRecord", key = "#id")
    })
    public void deleteBorrowingRecord(Long id) {
        borrowingRecordRepository.deleteById(id);
    }

    @Cacheable(value = "borrowingRecordsByCustomerId", key = "#userId")
    public Page<BorrowingRecord> getBorrowingRecordsByCustomerId(Long userId, int page, int size, String sortBy, String sortOrder) {
        Sort.Direction direction = sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return borrowingRecordRepository.findByCustomer_Id(userId, pageable);
    }

    @Cacheable(value = "borrowingRecordsByBookId", key = "#bookId")
    public Page<BorrowingRecord> getBorrowingRecordsByBookId(Long bookId, int page, int size, String sortBy, String sortOrder) {
        Sort.Direction direction = sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return borrowingRecordRepository.findByBook_Id(bookId, pageable);
    }
}

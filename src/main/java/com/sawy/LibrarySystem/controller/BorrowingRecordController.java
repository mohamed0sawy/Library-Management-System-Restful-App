package com.sawy.LibrarySystem.controller;

import com.sawy.LibrarySystem.model.BorrowingRecord;
import com.sawy.LibrarySystem.service.BorrowingRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BorrowingRecordController {

    private final BorrowingRecordService borrowingRecordService;

    @GetMapping("/borrowings")
    public ResponseEntity<Page<BorrowingRecord>> getAllBorrowingRecords(@RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "10") int size,
                                                                        @RequestParam(defaultValue = "id") String sortBy,
                                                                        @RequestParam(defaultValue = "asc") String sortOrder) {
        Page<BorrowingRecord> borrowingRecords = borrowingRecordService.getAllBorrowingRecords(page, size, sortBy, sortOrder);
        borrowingRecords.getContent().forEach(record -> {
            record.setCustomerID(record.getCustomer().getId());
            record.setBookID(record.getBook().getId());
        });
        return ResponseEntity.ok(borrowingRecords);
    }

    @GetMapping("/borrowings/{id}")
    public ResponseEntity<BorrowingRecord> getBorrowingRecordById(@PathVariable Long id) {
        Optional<BorrowingRecord> borrowingRecord = borrowingRecordService.getBorrowingRecordById(id);
        return borrowingRecord.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/borrowings")
    public ResponseEntity<BorrowingRecord> createBorrowingRecord(@RequestBody BorrowingRecord borrowingRecord) {
        BorrowingRecord createdBorrowingRecord = borrowingRecordService.createBorrowingRecord(borrowingRecord);
        return ResponseEntity.ok(createdBorrowingRecord);
    }

    @PutMapping("/borrowings/{id}")
    public ResponseEntity<BorrowingRecord> updateBorrowingRecord(@PathVariable Long id, @RequestBody BorrowingRecord borrowingRecordDetails) {
        Optional<BorrowingRecord> updatedBorrowingRecord = borrowingRecordService.updateBorrowingRecord(id, borrowingRecordDetails);
        return updatedBorrowingRecord.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/borrowings/{id}")
    public ResponseEntity<Void> deleteBorrowingRecord(@PathVariable Long id) {
        borrowingRecordService.deleteBorrowingRecord(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/borrowings/search")
    public ResponseEntity<Page<BorrowingRecord>> searchBorrowingRecords(@RequestParam(required = false) Long userId,
                                                                        @RequestParam(required = false) Long bookId,
                                                                        @RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "10") int size,
                                                                        @RequestParam(defaultValue = "id") String sortBy,
                                                                        @RequestParam(defaultValue = "asc") String sortOrder) {
        // Check if no parameters are provided
        if (userId == null && bookId == null) {
            return ResponseEntity.badRequest().build();
        }

        // Check if more than one parameter is provided
        if ((userId != null && bookId != null) || (userId != null && userId.toString().isEmpty()) || (bookId != null && bookId.toString().isEmpty())) {
            return ResponseEntity.badRequest().build();
        }

        // Search based on the provided parameter
        Page<BorrowingRecord> borrowingRecords;
        if (userId != null) {
            borrowingRecords = borrowingRecordService.getBorrowingRecordsByCustomerId(userId, page, size, sortBy, sortOrder);
        } else {
            borrowingRecords = borrowingRecordService.getBorrowingRecordsByBookId(bookId, page, size, sortBy, sortOrder);
        }
        return ResponseEntity.ok(borrowingRecords);
    }
}

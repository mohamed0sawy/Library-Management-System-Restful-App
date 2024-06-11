package com.sawy.LibrarySystem.repository;

import com.sawy.LibrarySystem.model.BorrowingRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord, Long> {
    Page<BorrowingRecord> findByCustomer_Id(Long userId, Pageable pageable);
    Page<BorrowingRecord> findByBook_Id(Long bookId, Pageable pageable);
}

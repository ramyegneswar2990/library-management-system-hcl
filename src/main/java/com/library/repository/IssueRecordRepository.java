package com.library.repository;

import com.library.model.IssueRecord;
import com.library.model.Member;
import com.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for IssueRecord operations.
 *
 * Common use cases:
 *   - Find all active issues (returnDate is null) for a member
 *   - Find the current issue record for a book (to process a return)
 */
@Repository
public interface IssueRecordRepository extends JpaRepository<IssueRecord, Long> {

    /** All issue records for a particular member */
    List<IssueRecord> findByMember(Member member);

    /** All issue records for a particular book */
    List<IssueRecord> findByBook(Book book);

    /**
     * Find the active (unreturned) issue record for a book.
     * returnDate == null means the book has not been returned yet.
     */
    Optional<IssueRecord> findByBookAndReturnDateIsNull(Book book);

    /** All currently active (unreturned) issues for a member */
    List<IssueRecord> findByMemberAndReturnDateIsNull(Member member);

    List<IssueRecord> findByMember_MemberIdAndReturnDateIsNull(Long memberId);

    Optional<IssueRecord> findByIssueIdAndReturnDateIsNull(Long issueId);
}
